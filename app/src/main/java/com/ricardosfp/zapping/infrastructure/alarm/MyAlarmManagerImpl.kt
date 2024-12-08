package com.ricardosfp.zapping.infrastructure.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.ricardosfp.zapping.infrastructure.AlarmReceiver
import com.ricardosfp.zapping.infrastructure.ApplicationClass
import com.ricardosfp.zapping.infrastructure.model.Alarm
import com.ricardosfp.zapping.infrastructure.util.ObjectToByte
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

// todo test
@Singleton
class MyAlarmManagerImpl @Inject constructor(@ApplicationContext val context: Context):
    MyAlarmManager {

    companion object {
        // alarm advance in minutes
        private const val ALARM_ADVANCE_MINUTES = -30
    }

    // todo this function could return something to indicate success/error
    override fun scheduleAlarm(alarm: Alarm) {

        (context as ApplicationClass).applicationScope.launch(Dispatchers.Default) {

            val alarmTime = Calendar.getInstance()
            alarmTime.time = alarm.matchDate

            // todo don't issue alarms for matches that have already finished
            //  I need to calculate that
            if (Calendar.getInstance().after(alarmTime)) {
                return@launch
            }

            val alarmBundle = Bundle()

            // https://commonsware.com/blog/2016/07/22/be-careful-where-you-use-custom-parcelables.html
            // https://stackoverflow.com/questions/39209579/how-to-pass-custom-serializable-object-to-broadcastreceiver-via-pendingintent
            alarmBundle.putByteArray(MyAlarmManager.BUNDLE_KEY, ObjectToByte.serialize(alarm))

            val alarmIntent = Intent(MyAlarmManager.RECEIVER_ACTION)
            alarmIntent.setClass(context, AlarmReceiver::class.java)

            // put the bundle in the intent
            alarmIntent.putExtras(alarmBundle)

            // https://stackoverflow.com/questions/10271417/android-alarmmanager-multiple-alarms-one-overwrites-the-other/10271488
            // https://stackoverflow.com/questions/17569896/android-alarmmanager-is-there-a-way-to-cancell-all-the-alarms-set
            // https://stackoverflow.com/questions/27637744/cancel-all-the-alarms-set-using-alarmmanager?noredirect=1&lq=1
            // I have to use different request codes or else each pending intent overwrites the previous
            val alarmPendingIntent = PendingIntent.getBroadcast(
                context,
                alarm.matchText.hashCode(),
                alarmIntent,
                PendingIntent.FLAG_IMMUTABLE)

            alarmTime.add(Calendar.MINUTE, ALARM_ADVANCE_MINUTES)
            Log.d("alarm", String.format("alarm set for %s", alarmTime.time.toString()))

            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // this could be called in the constructor, because it does not change during the lifetime of the app
                if (alarmMgr.canScheduleExactAlarms()) {
                    alarmMgr.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        alarmTime.timeInMillis,
                        alarmPendingIntent) // alarmMgr.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 15000, alarmPendingIntent);
                } else {
                    // schedule inexact alarms?
                    alarmMgr.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        alarmTime.timeInMillis,
                        alarmPendingIntent)
                }
            } else {
                alarmMgr.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmTime.timeInMillis,
                    alarmPendingIntent) // alarmMgr.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 15000, alarmPendingIntent);
            }
        }
    }
}
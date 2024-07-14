package com.ricardosfp.zapping.infrastructure.alarm

import android.app.*
import android.content.*
import android.os.*
import android.util.*
import com.ricardosfp.zapping.infrastructure.*
import com.ricardosfp.zapping.infrastructure.model.*
import com.ricardosfp.zapping.infrastructure.util.*
import dagger.hilt.android.qualifiers.*
import java.util.*
import javax.inject.*

@Singleton
class MyAlarmManagerImpl @Inject constructor(@ApplicationContext val context: Context): MyAlarmManager {

    override fun scheduleAlarm(alarm: Alarm) {
        val dateCalendar = Calendar.getInstance()
        dateCalendar.time = alarm.matchDate

        // don't issue alarms for matches that have already started
        if (Calendar.getInstance().after(dateCalendar)) {
            return
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
        val alarmPendingIntent = PendingIntent.getBroadcast(context, alarm.matchText.hashCode(), alarmIntent, PendingIntent.FLAG_IMMUTABLE)

        dateCalendar.add(Calendar.MINUTE, -30)
        Log.d("alarm", String.format("alarm set for %s", dateCalendar.time.toString()))

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmMgr.canScheduleExactAlarms()) {
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, dateCalendar.timeInMillis, alarmPendingIntent) // alarmMgr.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 15000, alarmPendingIntent);
            }
        } else {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, dateCalendar.timeInMillis, alarmPendingIntent) // alarmMgr.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 15000, alarmPendingIntent);
        }
    }

}
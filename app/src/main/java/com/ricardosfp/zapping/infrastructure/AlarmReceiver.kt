package com.ricardosfp.zapping.infrastructure

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ricardosfp.zapping.infrastructure.alarm.MyAlarmManager
import com.ricardosfp.zapping.infrastructure.model.Alarm
import com.ricardosfp.zapping.infrastructure.util.ObjectToByte.deserialize
import java.util.concurrent.ThreadLocalRandom

// todo test
class AlarmReceiver: BroadcastReceiver() {

    companion object {
        private val CLASS_NAME = AlarmReceiver::class.java.simpleName
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(CLASS_NAME, "alarm received")

        val byteArray = intent.getByteArrayExtra(MyAlarmManager.BUNDLE_KEY)

        if (byteArray != null) {
            try {
                val alarm = deserialize(byteArray) as Alarm

                // explanation about the changes to notification icons starting on Android 5
                // https://clevertap.com/blog/fixing-notification-icon-for-android-lollipop-and-above/
                // https://romannurik.github.io/AndroidAssetStudio/index.html
                val builder = NotificationCompat.Builder(context, NotificationChannel.MATCH.id)
//                    .setSmallIcon(R.drawable.)
                        .setContentTitle(NotificationChannel.MATCH.getName(context))
                        .setContentText(alarm.matchText)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                NotificationManagerCompat.from(context)
                        .notify(ThreadLocalRandom.current().nextInt(), builder.build())

            }
            catch (th: Throwable) {
                // todo report error
            }

        } else {
            // todo report error
        }
    }
}
package com.ricardosfp.zapping.infrastructure

import android.*
import android.content.*
import android.content.pm.*
import android.util.*
import androidx.core.app.*
import com.ricardosfp.zapping.infrastructure.alarm.*
import com.ricardosfp.zapping.infrastructure.model.*
import com.ricardosfp.zapping.infrastructure.util.ObjectToByte.deserialize
import java.util.concurrent.*

// todo test
class AlarmReceiver: BroadcastReceiver() {

    companion object {
        private val className = AlarmReceiver::class.java.simpleName
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(className, "alarm received")

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

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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
package org.ligi.materialteatimer.receiver

import android.app.AlarmManager.ELAPSED_REALTIME_WAKEUP
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import org.ligi.kaxt.getAlarmManager
import org.ligi.kaxt.getNotificationManager
import org.ligi.materialteatimer.MainActivity
import org.ligi.materialteatimer.R
import java.util.*

class TimerReceiver : BroadcastReceiver() {

    private val notificationId by lazy { Random().nextInt() }

    override fun onReceive(context: Context, intent: Intent?) {

        val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, "main")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentIntent(pendingIntent)
                .setContentText(context.getString(R.string.notification_text_tea_ready))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .build()

        context.getNotificationManager().notify(notificationId, notification)

        createAndInstallCancellation(context)
    }

    private fun createAndInstallCancellation(context: Context) {

        val triggerAtMillis = SystemClock.elapsedRealtime() + 10 * 60 * 1000

        val cancelIntent = Intent(context, CancelNotificationReceiver::class.java)
        cancelIntent.putExtra("id", notificationId)
        val pendingCancelIntent = PendingIntent.getBroadcast(context, 0, cancelIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        context.getAlarmManager().set(ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pendingCancelIntent)
    }

}
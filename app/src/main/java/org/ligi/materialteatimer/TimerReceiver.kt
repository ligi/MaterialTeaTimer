package org.ligi.materialteatimer

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat

class TimerReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT)


        val notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentIntent(pendingIntent)
                .setContentText(context.getString(R.string.notification_text_tea_ready))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)

        val systemService = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        systemService.notify(123, notificationBuilder.build())
    }

}
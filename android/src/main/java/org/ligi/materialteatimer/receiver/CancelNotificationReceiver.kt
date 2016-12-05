package org.ligi.materialteatimer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.ligi.kaxt.getNotificationManager
import org.ligi.materialteatimer.Timer

class CancelNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.getNotificationManager().cancel(intent.getIntExtra("id", 0))
        Timer.resetAndPause()
    }

}
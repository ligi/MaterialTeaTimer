package org.ligi.materialteatimer

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlarmManager.ELAPSED_REALTIME_WAKEUP
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.help.view.*
import org.ligi.compat.HtmlCompat
import org.ligi.kaxt.getAlarmManager
import org.ligi.kaxt.setExactAndAllowWhileIdleCompat
import org.ligi.materialteatimer.receiver.TimerReceiver
import kotlin.math.abs


class MainActivity : AppCompatActivity() {

    var handler = Handler()

    val pendingTimerReceiver: PendingIntent by lazy {
        val intent = Intent(applicationContext, TimerReceiver::class.java)
        PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    var defaultTextColor: Int = 0

    private var pause_state = !Timer.isPaused()

    val updater = object : Runnable {
        override fun run() {
            val remaining = TeaProvider.currentTea.brewTime - Timer.elapsedSeconds()

            val prefix = if (remaining < 0) {
                timer_min.setTextColor(Color.RED)
                timer_sec.setTextColor(Color.RED)
                "-"
            } else {
                timer_min.setTextColor(defaultTextColor)
                timer_sec.setTextColor(defaultTextColor)
                ""
            }

            timer_min.text = prefix + abs(remaining / 60).toString() + "m"
            timer_sec.text = abs(remaining % 60).toString() + "s"

            tea_progress.max = TeaProvider.currentTea.brewTime
            tea_progress.progress = Timer.elapsedSeconds().toInt()

            if (pause_state != Timer.isPaused()) {

                pause_state = Timer.isPaused()

                if (pause_state) {
                    getAlarmManager().cancel(pendingTimerReceiver)
                } else {
                    if (remaining > 0) {
                        val triggerAtMillis = SystemClock.elapsedRealtime() + remaining * 1000
                        getAlarmManager().setExactAndAllowWhileIdleCompat(ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pendingTimerReceiver)
                    }
                }

                if (Build.VERSION.SDK_INT >= 21) {
                    changeDrawableWithAnimation()
                } else {
                    val nextDrawable = if (pause_state) R.drawable.vectalign_vector_drawable_start else R.drawable.vectalign_vector_drawable_end
                    val drawable = VectorDrawableCompat.create(resources, nextDrawable, theme)
                    play_pause.setImageDrawable(drawable)
                }
            }
            invalidateOptionsMenu()

            handler.postDelayed(this, 50)
        }

        @TargetApi(21)
        private fun changeDrawableWithAnimation() {
            val drawable = getDrawable(if (pause_state) R.drawable.vectalign_animated_vector_drawable_end_to_start else R.drawable.vectalign_animated_vector_drawable_start_to_end) as AnimatedVectorDrawable
            play_pause.setImageDrawable(drawable)
            drawable.start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        defaultTextColor = timer_min.currentTextColor

        play_pause.setOnClickListener {
            Timer.togglePause()
        }

        fab.setOnClickListener {
            val listIntent = Intent(this, TeaListActivity::class.java)

            if (Build.VERSION.SDK_INT >= 21) {
                makeTransition(this, listIntent)
            } else {
                startActivity(listIntent)
            }
            handler.postDelayed({ finish() }, 1000)
        }
    }

    @TargetApi(21)
    private fun makeTransition(activity: Activity, intent: Intent) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, teaImage as View, getString(R.string.tea_transition_from_main))
        activity.startActivity(intent, options.toBundle())
    }

    override fun onPause() {
        handler.removeCallbacks(updater)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.resetTime).isVisible = Timer.elapsedSeconds() > 0
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.resetTime -> {
            Timer.resetAndPause()
            true
        }
        R.id.menuInfo -> {
            val view = layoutInflater.inflate(R.layout.help, null, false)
            view.helpText.text = HtmlCompat.fromHtml(getString(R.string.help))
            view.helpText.movementMethod = LinkMovementMethod()
            AlertDialog.Builder(this)
                    .setView(view)
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        handler.post(updater)
        TeaViewHolder(window.decorView).bind(TeaProvider.currentTea)
    }
}


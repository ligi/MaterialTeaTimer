package org.ligi.materialteatimer

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.ligi.compat.HtmlCompat


class MainActivity : AppCompatActivity() {

    var handler = Handler()

    val alarmManager by lazy { getSystemService(Context.ALARM_SERVICE) as AlarmManager }
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

            timer_min.text = prefix + Math.abs(remaining / 60).toString() + "m"
            timer_sec.text = Math.abs(remaining % 60).toString() + "s"

            tea_progress.max = TeaProvider.currentTea.brewTime
            tea_progress.progress = Timer.elapsedSeconds().toInt()

            if (pause_state != Timer.isPaused()) {

                pause_state = Timer.isPaused()

                if (pause_state) {
                    alarmManager.cancel(pendingTimerReceiver)
                } else {
                    if (remaining > 0) {
                        if (Build.VERSION.SDK_INT >= 19) {
                            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + remaining * 1000, pendingTimerReceiver)
                        } else {
                            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + remaining * 1000, pendingTimerReceiver)
                        }
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
            supportInvalidateOptionsMenu()

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
            val textView = layoutInflater.inflate(R.layout.help, null, false).findViewById(R.id.helpText) as TextView
            textView.text = HtmlCompat.fromHtml(getString(R.string.help))
            textView.movementMethod = LinkMovementMethod()
            AlertDialog.Builder(this)
                    .setView(textView)
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


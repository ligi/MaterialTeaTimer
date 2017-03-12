package org.ligi.materialteatimer

import android.os.SystemClock

object Timer {

    var startTime = SystemClock.elapsedRealtime()
    var pauseTime: Long? = SystemClock.elapsedRealtime()

    fun resetAndPause() {
        pauseTime = SystemClock.elapsedRealtime()
        startTime = SystemClock.elapsedRealtime()
    }

    fun togglePause() {
        if (pauseTime == null) {
            pauseTime = SystemClock.elapsedRealtime()
        } else {
            startTime = SystemClock.elapsedRealtime() - (pauseTime!! - startTime)
            pauseTime = null
        }
    }

    fun elapsedSeconds(): Long {
        val baseTime = pauseTime ?: SystemClock.elapsedRealtime()
        return (baseTime - startTime) / 1000
    }

    fun isPaused() = pauseTime != null


}
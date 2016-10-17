package org.ligi.materialteatimer

object Timer {

    var startTime = System.currentTimeMillis()
    var pauseTime: Long? = System.currentTimeMillis()

    fun resetAndPause() {
        pauseTime = System.currentTimeMillis()
        startTime = System.currentTimeMillis()
    }

    fun togglePause() {
        if (pauseTime == null) {
            pauseTime = System.currentTimeMillis()
        } else {
            startTime = System.currentTimeMillis() - (pauseTime!! - startTime)
            pauseTime = null
        }
    }

    fun elapsedSeconds(): Long {
        val baseTime = pauseTime ?: System.currentTimeMillis()
        return (baseTime - startTime) / 1000
    }

    fun isPaused(): Boolean {
        return pauseTime != null
    }


}
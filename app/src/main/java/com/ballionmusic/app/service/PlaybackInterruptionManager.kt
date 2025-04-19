package com.ballionmusic.app.service

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.ballionmusic.app.data.VIPStatusManager

class PlaybackInterruptionManager(
    private val context: Context,
    private val onInterruptionStart: () -> Unit,
    private val onInterruptionEnd: () -> Unit
) {

    private val vipStatusManager = VIPStatusManager(context)
    private val handler = Handler(Looper.getMainLooper())

    private var playbackTimeMillis: Long = 0L
    private var interruptionScheduled = false

    private val interruptionRunnable = object : Runnable {
        override fun run() {
            if (!vipStatusManager.isVIP()) {
                onInterruptionStart()
                handler.postDelayed({
                    onInterruptionEnd()
                    playbackTimeMillis = 0L
                    scheduleInterruption()
                }, INTERRUPTION_DURATION)
            }
        }
    }

    companion object {
        private const val INTERRUPTION_INTERVAL = 10 * 60 * 1000L // 10 minutes
        private const val INTERRUPTION_DURATION = 60 * 1000L // 1 minute
    }

    fun startTracking() {
        playbackTimeMillis = 0L
        scheduleInterruption()
    }

    fun stopTracking() {
        handler.removeCallbacks(interruptionRunnable)
        interruptionScheduled = false
    }

    fun onPlaybackProgress(elapsedMillis: Long) {
        if (vipStatusManager.isVIP()) {
            stopTracking()
            return
        }
        playbackTimeMillis += elapsedMillis
        if (!interruptionScheduled && playbackTimeMillis >= INTERRUPTION_INTERVAL) {
            interruptionScheduled = true
            handler.post(interruptionRunnable)
        }
    }

    private fun scheduleInterruption() {
        interruptionScheduled = false
        handler.postDelayed(interruptionRunnable, INTERRUPTION_INTERVAL)
    }
}

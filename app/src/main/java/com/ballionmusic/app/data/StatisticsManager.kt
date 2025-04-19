package com.ballionmusic.app.data

import android.content.Context
import android.content.SharedPreferences

class StatisticsManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("stats_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOTAL_LISTENING_TIME = "total_listening_time" // in milliseconds
        private const val KEY_TRACK_PREFIX = "track_" // prefix for per-track listening time
    }

    fun addListeningTime(trackId: Long, timeMillis: Long) {
        val totalTime = prefs.getLong(KEY_TOTAL_LISTENING_TIME, 0L) + timeMillis
        prefs.edit().putLong(KEY_TOTAL_LISTENING_TIME, totalTime).apply()

        val trackKey = "$KEY_TRACK_PREFIX$trackId"
        val trackTime = prefs.getLong(trackKey, 0L) + timeMillis
        prefs.edit().putLong(trackKey, trackTime).apply()
    }

    fun getTotalListeningTime(): Long {
        return prefs.getLong(KEY_TOTAL_LISTENING_TIME, 0L)
    }

    fun getTrackListeningTime(trackId: Long): Long {
        val trackKey = "$KEY_TRACK_PREFIX$trackId"
        return prefs.getLong(trackKey, 0L)
    }

    fun resetStatistics() {
        prefs.edit().clear().apply()
    }
}

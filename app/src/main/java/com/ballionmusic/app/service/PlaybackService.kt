ты вpackage com.ballionmusic.app.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ballionmusic.app.R
import com.ballionmusic.app.ui.MainActivity
import com.ballionmusic.app.service.PlaybackInterruptionManager

class PlaybackService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private val binder = LocalBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var currentTrackIndex = 0
    private var playlist: List<String> = emptyList() // List of track file paths

    private lateinit var interruptionManager: PlaybackInterruptionManager
    private var isInterrupted = false

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService(): PlaybackService = this@PlaybackService
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.setOnCompletionListener(this)
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)

        interruptionManager = PlaybackInterruptionManager(
            this,
            onInterruptionStart = {
                pause()
                isInterrupted = true
                // TODO: Show VIP ad message UI or notification
            },
            onInterruptionEnd = {
                isInterrupted = false
                resume()
                // TODO: Hide VIP ad message UI or notification
            }
        )
    }

    fun setPlaylist(tracks: List<String>) {
        playlist = tracks
        currentTrackIndex = 0
    }

    fun play() {
        if (playlist.isEmpty()) return
        val trackPath = playlist[currentTrackIndex]
        mediaPlayer?.reset()
        mediaPlayer?.setDataSource(trackPath)
        mediaPlayer?.prepareAsync()
        startForeground(1, buildNotification("Loading..."))
        interruptionManager.startTracking()
    }

    fun pause() {
        mediaPlayer?.pause()
        stopForeground(false)
        interruptionManager.stopTracking()
    }

    fun resume() {
        if (!isInterrupted) {
            mediaPlayer?.start()
            startForeground(1, buildNotification(getCurrentTrackTitle()))
            interruptionManager.startTracking()
        }
    }

    fun rewind10Sec() {
        mediaPlayer?.let {
            val newPos = (it.currentPosition - 10000).coerceAtLeast(0)
            it.seekTo(newPos)
        }
    }

    fun forward10Sec() {
        mediaPlayer?.let {
            val newPos = (it.currentPosition + 10000).coerceAtMost(it.duration)
            it.seekTo(newPos)
        }
    }

    // Fix for track skip bug when opening player control menu
    fun onPlayerControlMenuOpened() {
        // Prevent unintended seek by ignoring seekTo calls triggered by menu open
        // Implementation depends on UI interaction, here we just ignore seekTo calls for a short time
        ignoreSeek = true
        seekIgnoreHandler.postDelayed({
            ignoreSeek = false
        }, SEEK_IGNORE_DURATION)
    }

    private var ignoreSeek = false
    private val seekIgnoreHandler = android.os.Handler(android.os.Looper.getMainLooper())
    private val SEEK_IGNORE_DURATION = 1000L // 1 second

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer?.start()
        startForeground(1, buildNotification(getCurrentTrackTitle()))
    }

    fun safeSeekTo(position: Int) {
        if (!ignoreSeek) {
            mediaPlayer?.seekTo(position)
        }
    }

    fun nextTrack() {
        if (playlist.isEmpty()) return
        currentTrackIndex = (currentTrackIndex + 1) % playlist.size
        play()
    }

    fun previousTrack() {
        if (playlist.isEmpty()) return
        currentTrackIndex = if (currentTrackIndex - 1 < 0) playlist.size - 1 else currentTrackIndex - 1
        play()
    }

    private fun getCurrentTrackTitle(): String {
        // TODO: Implement actual track title retrieval
        return "Track ${currentTrackIndex + 1}"
    }

    private fun buildNotification(trackTitle: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, "playback_channel")
            .setContentTitle(trackTitle)
            .setContentText("BallionMusic")
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_rewind_10, "Rewind 10s", null) // TODO: Add PendingIntent for action
            .addAction(R.drawable.ic_pause, "Pause", null) // TODO: Add PendingIntent for action
            .addAction(R.drawable.ic_forward_10, "Forward 10s", null) // TODO: Add PendingIntent for action
            .addAction(R.drawable.ic_skip_previous, "Previous", null) // TODO: Add PendingIntent for action
            .addAction(R.drawable.ic_skip_next, "Next", null) // TODO: Add PendingIntent for action
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer?.start()
        startForeground(1, buildNotification(getCurrentTrackTitle()))
    }

    override fun onCompletion(mp: MediaPlayer?) {
        nextTrack()
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
}

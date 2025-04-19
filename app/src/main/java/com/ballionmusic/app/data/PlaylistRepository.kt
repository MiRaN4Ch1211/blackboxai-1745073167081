package com.ballionmusic.app.data

import android.content.Context
import com.ballionmusic.app.model.Playlist

class PlaylistRepository(private val context: Context) {

    private val playlists = mutableListOf<Playlist>()

    fun getPlaylists(): List<Playlist> {
        return playlists.toList()
    }

    fun addPlaylist(playlist: Playlist): Boolean {
        // Restriction: max 3 playlists for non-VIP users
        val isVIP = checkVIPStatus()
        if (!isVIP && playlists.size >= 3) {
            return false
        }
        playlists.add(playlist)
        return true
    }

    fun deletePlaylist(playlistId: Long) {
        playlists.removeAll { it.id == playlistId }
    }

    private fun checkVIPStatus(): Boolean {
        // TODO: Implement actual VIP status check
        return false
    }
}

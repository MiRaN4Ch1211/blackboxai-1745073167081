package com.ballionmusic.app.data

import android.content.Context
import android.content.SharedPreferences
import com.ballionmusic.app.model.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalStorageManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_PLAYLISTS = "playlists"
    }

    fun savePlaylists(playlists: List<Playlist>) {
        val json = gson.toJson(playlists)
        prefs.edit().putString(KEY_PLAYLISTS, json).apply()
    }

    fun loadPlaylists(): List<Playlist> {
        val json = prefs.getString(KEY_PLAYLISTS, null) ?: return emptyList()
        val type = object : TypeToken<List<Playlist>>() {}.type
        return gson.fromJson(json, type)
    }
}

package com.ballionmusic.app.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ballionmusic.app.databinding.PlaylistItemBinding
import com.ballionmusic.app.model.Playlist

class PlaylistAdapter(
    private val playlists: List<Playlist>,
    private val onItemClick: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(val binding: PlaylistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) {
            binding.tvPlaylistName.text = playlist.name
            binding.root.setOnClickListener {
                onItemClick(playlist)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size
}

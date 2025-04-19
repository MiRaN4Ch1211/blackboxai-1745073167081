package com.ballionmusic.app.ui.playlist

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ballionmusic.app.data.PlaylistRepository
import com.ballionmusic.app.databinding.PlaylistFragmentBinding
import com.ballionmusic.app.model.Playlist

class PlaylistFragment : Fragment() {

    private var _binding: PlaylistFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlistRepository: PlaylistRepository
    private val playlists = mutableListOf<Playlist>()
    private lateinit var adapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistRepository = PlaylistRepository(requireContext())
        playlists.addAll(playlistRepository.getPlaylists())

        adapter = PlaylistAdapter(playlists) { playlist ->
            // Handle playlist item click (e.g., open playlist)
        }

        binding.rvPlaylists.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlaylists.adapter = adapter

        binding.btnAddPlaylist.setOnClickListener {
            showAddPlaylistDialog()
        }
    }

    private fun showAddPlaylistDialog() {
        val editText = EditText(requireContext())
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("New Playlist")
            .setMessage("Enter playlist name:")
            .setView(editText)
            .setPositiveButton("Create") { _, _ ->
                val name = editText.text.toString().trim()
                if (name.isNotEmpty()) {
                    val newPlaylist = Playlist(
                        id = System.currentTimeMillis(),
                        name = name
                    )
                    val success = playlistRepository.addPlaylist(newPlaylist)
                    if (success) {
                        playlists.add(newPlaylist)
                        adapter.notifyItemInserted(playlists.size - 1)
                    } else {
                        Toast.makeText(requireContext(), "Playlist limit reached for non-VIP users", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Playlist name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.ballionmusic.app.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ballionmusic.app.databinding.ThemeFragmentBinding

class ThemeFragment : Fragment() {

    private var _binding: ThemeFragmentBinding? = null
    private val binding get() = _binding!!

    private val themes = listOf(
        "Light",
        "Dark",
        "Gray",
        "Light Gray",
        "Dark Gray",
        "Light White",
        "Dark White",
        "Purple",
        "Pink",
        "Red-Purple",
        "Orange"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ThemeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvThemes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvThemes.adapter = ThemeAdapter(themes) { theme ->
            // TODO: Handle theme selection and apply theme change
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

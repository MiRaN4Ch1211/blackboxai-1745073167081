package com.ballionmusic.app.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ballionmusic.app.databinding.LanguageFragmentBinding

class LanguageFragment : Fragment() {

    private var _binding: LanguageFragmentBinding? = null
    private val binding get() = _binding!!

    private val languages = listOf(
        "Azerbaijani",
        "Arabic",
        "Belarusian",
        "Belgian",
        "English",
        "French",
        "German",
        "Hindi",
        "Polish",
        "Russian",
        "Ukrainian"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LanguageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLanguages.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLanguages.adapter = LanguageAdapter(languages) { language ->
            // TODO: Handle language selection and apply language change
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

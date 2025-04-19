package com.ballionmusic.app.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ballionmusic.app.databinding.LanguageItemBinding

class LanguageAdapter(
    private val languages: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(val binding: LanguageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(language: String) {
            binding.tvLanguageName.text = language
            binding.root.setOnClickListener {
                onItemClick(language)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = LanguageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(languages[position])
    }

    override fun getItemCount(): Int = languages.size
}

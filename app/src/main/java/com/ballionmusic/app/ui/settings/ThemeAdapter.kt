package com.ballionmusic.app.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ballionmusic.app.databinding.ThemeItemBinding

class ThemeAdapter(
    private val themes: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder>() {

    inner class ThemeViewHolder(val binding: ThemeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(theme: String) {
            binding.tvThemeName.text = theme
            binding.root.setOnClickListener {
                onItemClick(theme)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val binding = ThemeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThemeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        holder.bind(themes[position])
    }

    override fun getItemCount(): Int = themes.size
}

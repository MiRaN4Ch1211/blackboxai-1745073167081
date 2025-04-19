package com.ballionmusic.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ballionmusic.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Initialize UI components and navigation
    }
}

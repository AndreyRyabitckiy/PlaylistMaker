package com.example.playlistmaker.main.presentation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.main.presentation.view_model.MainViewModel
import com.example.playlistmaker.media.presentation.activity.MediaLibraryActivity
import com.example.playlistmaker.search.presentation.activity.SearchActivity
import com.example.playlistmaker.settings.presentation.activity.SettingActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            bButtonSearch.setOnClickListener {
                val displayIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(displayIntent)
            }

            bButtonMedia.setOnClickListener {
                val displayIntent = Intent(this@MainActivity, MediaLibraryActivity::class.java)
                startActivity(displayIntent)
            }

            bButtonSettings.setOnClickListener {
                val displayIntent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(displayIntent)
            }
        }

        viewModel.theme.observe(this) {
            AppCompatDelegate.setDefaultNightMode(it)
        }

        viewModel.requestTheme()
    }
}

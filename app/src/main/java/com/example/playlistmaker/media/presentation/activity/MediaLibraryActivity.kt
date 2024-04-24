package com.example.playlistmaker.media.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            toolBar.setOnClickListener {
                finish()
            }

            binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

            tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
                when(position){
                    0 -> tab.text = getString(R.string.likeMusic)
                    1 -> tab.text = getString(R.string.playListMusic)
                }
            }

            tabMediator.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}
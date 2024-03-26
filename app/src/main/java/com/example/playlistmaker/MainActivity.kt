package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.presentation.search.SearchActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.bButtonSearch)
        val buttonMedia = findViewById<Button>(R.id.bButtonMedia)
        val buttonSettings = findViewById<Button>(R.id.bButtonSettings)

        buttonSearch.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        buttonMedia.setOnClickListener {
            val displayIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(displayIntent)
        }

        buttonSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingActivity::class.java)
            startActivity(displayIntent)
        }
    }
}

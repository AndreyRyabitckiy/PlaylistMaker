package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SerarchActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serarch)

        val backButton = findViewById<ImageView>(R.id.toolBar)

        backButton.setOnClickListener {
            val displayIntent = Intent(this,MainActivity::class.java)
            startActivity(displayIntent)
        }
    }
}
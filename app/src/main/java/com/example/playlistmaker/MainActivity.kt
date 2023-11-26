package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1 = findViewById<Button>(R.id.buttonPanel1)
        val button2 = findViewById<Button>(R.id.buttonPanel2)
        val button3 = findViewById<Button>(R.id.buttonPanel3)

        button1.setOnClickListener {
            val displayIntent = Intent(this,SerarchActivity::class.java)
            startActivity(displayIntent)
        }

        button2.setOnClickListener {
            val displayIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(displayIntent)
        }

        button3.setOnClickListener {
            val displayIntent = Intent(this, SettingActivity::class.java)
            startActivity(displayIntent)
        }


    }
}

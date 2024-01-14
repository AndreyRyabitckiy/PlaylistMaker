package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val backButton = findViewById<ImageView>(R.id.ivToolBar)
        val shareButton = findViewById<LinearLayout>(R.id.llShareApp)
        val sendButton = findViewById<LinearLayout>(R.id.llSendSuport)
        val userPolicButton = findViewById<LinearLayout>(R.id.llUserPolic)
        val switch = findViewById<SwitchMaterial>(R.id.sDayornight)

        backButton.setOnClickListener {
            finish()
        }

        val sharedPrefs = getSharedPreferences(App.THEME_PREFERENCES, MODE_PRIVATE)
        val themeDayOrNight:Boolean = false
        switch.isChecked = sharedPrefs.getBoolean(App.DAY_NIGHT,themeDayOrNight)

        shareButton.setOnClickListener {
            val ShareButton: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.shareHT))
                setType("text/plain")
            }
            startActivity(ShareButton)
        }

        sendButton.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.emailSubject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.emailText))
                startActivity(this)
            }
        }

        userPolicButton.setOnClickListener {
            val userPolicOpen: Intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.userPolicWeb)))
            startActivity(userPolicOpen)
        }

        switch.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }
}
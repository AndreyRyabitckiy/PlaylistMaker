package com.example.playlistmaker.settings.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {

    private val sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override fun getThemeSettings() =
        sharedPrefs.getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    override fun updateThemeSetting(theme: Int) =
        sharedPrefs.edit().putInt(THEME_KEY, theme).apply()


    override fun getAppShareLink() = context.getString(R.string.shareHT)

    override fun getUserPolicy() = context.getString(R.string.userPolicWeb)

    override fun getSupportEmail() = context.getString(R.string.email)
    override fun getSupportEmailTop() = context.getString(R.string.emailSubject)
    override fun getSupportEmailText() = context.getString(R.string.emailText)


    companion object {
        private const val PREF_NAME = "PREF_SETTINGS"
        private const val THEME_KEY = "THEME_KEY"
    }
}
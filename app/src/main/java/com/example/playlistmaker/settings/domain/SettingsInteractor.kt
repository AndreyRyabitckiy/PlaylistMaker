package com.example.playlistmaker.settings.domain

interface SettingsInteractor {
    fun getThemeSettings(): Int
    fun updateThemeSetting(theme: Int)
}
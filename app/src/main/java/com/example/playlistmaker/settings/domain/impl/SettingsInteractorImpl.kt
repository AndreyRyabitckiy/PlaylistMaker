package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {

    override fun getThemeSettings(): Int = repository.getThemeSettings()

    override fun updateThemeSetting(theme: Int) = repository.updateThemeSetting(theme)

}
package com.example.playlistmaker.settings.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {


    private val _theme = MutableLiveData<Int>()
    val theme: LiveData<Int>
        get() = _theme


    init {
        getTheme()
    }

    fun updateTheme(theme: Int) {
        settingsInteractor.updateThemeSetting(theme)
        getTheme()
    }

    private fun getTheme() {
        _theme.postValue(settingsInteractor.getThemeSettings())
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun sendToSupport() {
        sharingInteractor.openSupport()
    }

    fun userPolicy() {
        sharingInteractor.openTerms()
    }
}
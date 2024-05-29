package com.example.playlistmaker.media.presentation.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor

class MediaLibraryViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {
    private val _theme = MutableLiveData(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    val theme: LiveData<Int>
        get() = _theme


    fun requestTheme() {
        _theme.postValue(settingsInteractor.getThemeSettings())
    }
}
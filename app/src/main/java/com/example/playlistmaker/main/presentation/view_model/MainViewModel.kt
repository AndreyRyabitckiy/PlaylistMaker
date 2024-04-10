package com.example.playlistmaker.main.presentation.view_model

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl


class MainViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    private val _theme = MutableLiveData(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    val theme: LiveData<Int>
        get() = _theme


    fun requestTheme() {
        _theme.postValue(settingsInteractor.getThemeSettings())
    }

//    class Factory(private val context: Context) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return MainViewModel(
//                SettingsInteractorImpl(Creator.settingRepository(context))
//            ) as T
//        }
//    }
}
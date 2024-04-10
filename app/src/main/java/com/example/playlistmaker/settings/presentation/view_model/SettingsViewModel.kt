package com.example.playlistmaker.settings.presentation.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SharingInteractor
import com.example.playlistmaker.settings.domain.impl.SharingInteractorImpl

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


//    class Factory(private val context: Context) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            val settingsRepository = Creator.settingRepository(context)
//            return SettingsViewModel(
//                sharingInteractor = SharingInteractorImpl(
//                    externalNavigator = Creator.externalNavigator(context),
//                    settingsRepository = settingsRepository
//                ),
//                settingsInteractor = SettingsInteractorImpl(settingsRepository)
//            ) as T
//        }
//    }

}
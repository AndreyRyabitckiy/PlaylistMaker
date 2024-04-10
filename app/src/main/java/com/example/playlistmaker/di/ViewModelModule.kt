package com.example.playlistmaker.di

import com.example.playlistmaker.main.presentation.view_model.MainViewModel
import com.example.playlistmaker.player.presentation.view_model.MusicPlayerViewModel
import com.example.playlistmaker.search.presentation.view_model.SearchActivityViewModel
import com.example.playlistmaker.settings.presentation.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<MainViewModel> {
        MainViewModel(
            settingsInteractor = get()
        )
    }

    viewModel<MusicPlayerViewModel> {
        MusicPlayerViewModel()
    }

    viewModel<SearchActivityViewModel> {
        SearchActivityViewModel(
            sharedPrefsInteractor = get(),
            tracksInteractor = get()
        )
    }

    viewModel<SettingsViewModel>{
        SettingsViewModel(
            sharingInteractor = get(),
            settingsInteractor = get()
        )
    }
}
package com.example.playlistmaker.di

import com.example.playlistmaker.media.presentation.view_model.LikeMusicViewModel
import com.example.playlistmaker.media.presentation.view_model.MediaLibraryViewModel
import com.example.playlistmaker.player.presentation.view_model.MusicPlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.view_model.SearchFragmentViewModel
import com.example.playlistmaker.settings.presentation.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<MusicPlayerViewModel> { (track: Track) ->
        MusicPlayerViewModel(track, get(), get())
    }

    viewModel<LikeMusicViewModel> {
        LikeMusicViewModel(get())
    }

    viewModel<SearchFragmentViewModel> {
        SearchFragmentViewModel(
            sharedPrefsInteractor = get(),
            tracksInteractor = get()
        )
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            sharingInteractor = get(),
            settingsInteractor = get()
        )
    }

    viewModel<MediaLibraryViewModel> {
        MediaLibraryViewModel(get())
    }
}
package com.example.playlistmaker.di

import com.example.playlistmaker.about_playlist.presentation.view_model.AboutPlayListFragmentViewModel
import com.example.playlistmaker.media.presentation.view_model.LikeMusicViewModel
import com.example.playlistmaker.media.presentation.view_model.MediaLibraryViewModel
import com.example.playlistmaker.media.presentation.view_model.PlayListViewModel
import com.example.playlistmaker.player.presentation.view_model.MusicPlayerViewModel
import com.example.playlistmaker.playlist_create.domain.models.PlayList
import com.example.playlistmaker.playlist_create.presentation.view_model.CreatePlayListFragmentViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.view_model.SearchFragmentViewModel
import com.example.playlistmaker.settings.presentation.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<MusicPlayerViewModel> { (track: Track) ->
        MusicPlayerViewModel(track, get(), get(), get())
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

    viewModel<PlayListViewModel> {
        PlayListViewModel(
            get()
        )
    }

    viewModel<CreatePlayListFragmentViewModel> {
        CreatePlayListFragmentViewModel(
            playListInteractor = get(),
            saveImageInteractor = get()
        )
    }

    viewModel<AboutPlayListFragmentViewModel>{
        AboutPlayListFragmentViewModel(
            get()
        )
    }
}
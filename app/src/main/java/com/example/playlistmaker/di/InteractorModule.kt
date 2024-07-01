package com.example.playlistmaker.di

import com.example.playlistmaker.db.domain.LikeTrackInteractor
import com.example.playlistmaker.db.domain.PlayListInteractor
import com.example.playlistmaker.db.domain.impl.LikeTrackInteractorImpl
import com.example.playlistmaker.db.domain.impl.PlayListInteractorImpl
import com.example.playlistmaker.playlist_create.domain.SaveImageToMemoryInteractor
import com.example.playlistmaker.playlist_create.domain.impl.SaveImageToMemoryInteractorImpl
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.impl.SharedPrefsInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.sharedprefs.SharedPrefsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SharingInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interatorModule = module {

    factory<SettingsInteractor> {
        SettingsInteractorImpl(repository = get())
    }

    factory<SharedPrefsInteractor> {
        SharedPrefsInteractorImpl(repository = get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(externalNavigator = get(), settingsRepository = get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(repository = get())
    }

    factory<LikeTrackInteractor> {
        LikeTrackInteractorImpl(get())
    }

    factory<PlayListInteractor> {
        PlayListInteractorImpl(get())
    }

    factory<SaveImageToMemoryInteractor> {
        SaveImageToMemoryInteractorImpl(get())
    }
}
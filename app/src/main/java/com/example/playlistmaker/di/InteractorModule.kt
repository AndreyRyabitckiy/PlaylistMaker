package com.example.playlistmaker.di

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
}
package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.SharedPrefsRepositoryImpl
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.sharedprefs.SharedPrefsRepository
import com.example.playlistmaker.settings.data.ExternalNavigatorImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.ExternalNavigator
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(context = androidContext())
    }

    single<SharedPrefsRepository> {
        SharedPrefsRepositoryImpl(storage = get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = androidContext())
    }

    single<TracksRepository> {
        TrackRepositoryImpl(networkClient = get())
    }

}
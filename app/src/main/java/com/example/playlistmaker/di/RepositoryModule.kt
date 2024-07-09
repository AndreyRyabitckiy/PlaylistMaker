package com.example.playlistmaker.di

import com.example.playlistmaker.db.data.LikeTrackRepositoryImpl
import com.example.playlistmaker.db.data.PlayListRepositoryImpl
import com.example.playlistmaker.db.data.converters.PlayListDbConvertor
import com.example.playlistmaker.db.data.converters.TrackDbConvertor
import com.example.playlistmaker.db.domain.LikeTrackRepository
import com.example.playlistmaker.db.domain.PlayListRepository
import com.example.playlistmaker.playlist_create.data.SaveImageToMemoryRepositoryImpl
import com.example.playlistmaker.playlist_create.domain.SaveImageToMemoryRepository
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

    single<LikeTrackRepository> {
        LikeTrackRepositoryImpl(get(), get())
    }

    single<SharedPrefsRepository> {
        SharedPrefsRepositoryImpl(storage = get(), likeTrackDatabase = get())
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(context = androidContext())
    }

    single<TracksRepository> {
        TrackRepositoryImpl(networkClient = get(), likeTrackDatabase = get())
    }

    single<SaveImageToMemoryRepository> {
        SaveImageToMemoryRepositoryImpl(get())
    }

    factory { TrackDbConvertor() }

    factory { PlayListDbConvertor() }

    single<PlayListRepository> {
        PlayListRepositoryImpl(get(), get(), androidContext())
    }

}
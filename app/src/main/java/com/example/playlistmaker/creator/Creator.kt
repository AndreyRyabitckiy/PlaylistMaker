package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.search.data.SharedPrefsRepositoryImpl
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.sharedstorage.SearchHistoryStorageImpl
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.SharedPrefsInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.sharedprefs.SharedPrefsInteractor
import com.example.playlistmaker.search.domain.sharedprefs.SharedPrefsRepository
import com.example.playlistmaker.settings.data.ExternalNavigatorImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.ExternalNavigator
import com.example.playlistmaker.settings.domain.SettingsRepository

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSharedPrefsRepository(context: Context): SharedPrefsRepository {
        return SharedPrefsRepositoryImpl(SearchHistoryStorageImpl(context))
    }

    fun SharedPrefsInteractor(context: Context): SharedPrefsInteractor {
        return SharedPrefsInteractorImpl(getSharedPrefsRepository(context))
    }

    fun settingRepository(context: Context): SettingsRepository = SettingsRepositoryImpl(context)

    fun externalNavigator(context: Context): ExternalNavigator = ExternalNavigatorImpl(context)
}
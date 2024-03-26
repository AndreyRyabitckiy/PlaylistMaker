package com.example.playlistmaker

import com.example.playlistmaker.data.SharedPrefsRepositoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.sharedstorage.SearchHistoryStorageImpl
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.SharedPrefsInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.sharedprefs.SharedPrefsInteractor
import com.example.playlistmaker.domain.sharedprefs.SharedPrefsRepository

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSharedPrefsRepository(): SharedPrefsRepository {
        return SharedPrefsRepositoryImpl(SearchHistoryStorageImpl())
    }

    fun SharedPrefsInteractor(): SharedPrefsInteractor {
        return SharedPrefsInteractorImpl(getSharedPrefsRepository())
    }
}
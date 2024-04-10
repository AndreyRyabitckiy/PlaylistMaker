package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.SearchHistoryStorage
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.sharedstorage.SearchHistoryStorageImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<SearchHistoryStorage> {
        SearchHistoryStorageImpl(context = androidContext())
    }

    single<NetworkClient> {
        RetrofitNetworkClient()
    }
}
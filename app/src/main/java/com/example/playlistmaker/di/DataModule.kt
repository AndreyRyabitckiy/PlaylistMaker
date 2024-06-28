package com.example.playlistmaker.di

import android.content.Context
import androidx.room.Room
import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.SearchHistoryStorage
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.sharedstorage.SearchHistoryStorageImpl
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val HISTORY_MAIN = "historyMain"

val dataModule = module {
    single<SearchHistoryStorage> {
        SearchHistoryStorageImpl(get(), get())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }
    single { provideOkHttpClient() }
    single { provideRetrofit(baseUrl = BASE_URL, okHttpClient = get()) }
    single { provideITunesApi(get()) }

    single { provideGson() }

    single {
        androidContext().getSharedPreferences(
            HISTORY_MAIN,
            Context.MODE_PRIVATE
        )
    }
}

private const val BASE_URL = "https://itunes.apple.com"
private fun provideITunesApi(builder: Retrofit) =
    builder.create(com.example.playlistmaker.search.data.network.ITunesApi::class.java)

private fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient) = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private fun provideOkHttpClient() = OkHttpClient.Builder()
    .readTimeout(3, TimeUnit.SECONDS)
    .writeTimeout(3, TimeUnit.SECONDS)
    .connectTimeout(3, TimeUnit.SECONDS)
    .build()

private fun provideGson() = Gson()
package com.example.playlistmaker.domain.impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.sharedprefs.SharedPrefsInteractor
import com.example.playlistmaker.domain.sharedprefs.SharedPrefsRepository

class SharedPrefsInteractorImpl(private val repository: SharedPrefsRepository) :
    SharedPrefsInteractor {
    override fun readWriteClear(
        sharedPreferences: SharedPreferences,
        use: String,
        track: Track?,
        consumer: SharedPrefsInteractor.SharedPrefsConsumer
    ) {
        consumer.consume(repository.saveReadClear(sharedPreferences, use, track))
    }
}
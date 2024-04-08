package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.sharedprefs.SharedPrefsInteractor
import com.example.playlistmaker.search.domain.sharedprefs.SharedPrefsRepository

class SharedPrefsInteractorImpl(private val repository: SharedPrefsRepository) :
    SharedPrefsInteractor {
    override fun readWriteClear(
        use: String,
        track: Track?,
        consumer: SharedPrefsInteractor.SharedPrefsConsumer
    ) {
        consumer.consume(repository.saveReadClear(use, track))
    }
}
package com.example.playlistmaker.search.domain.sharedprefs

import com.example.playlistmaker.search.domain.models.Track

interface SharedPrefsInteractor {
    suspend fun readWriteClear(
        use: String,
        track: Track?,
        consumer: SharedPrefsConsumer
    )

    suspend fun readWriteClearWithoutConsumer(
        use: String,
        track: Track?
    ): List<Track>

    interface SharedPrefsConsumer {
        fun consume(foundSharedPrefs: ArrayList<Track>)
    }
}
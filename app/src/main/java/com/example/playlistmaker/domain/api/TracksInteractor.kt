package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.ResponseStatus
import com.example.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTrack: List<Track>, status: ResponseStatus)
    }
}
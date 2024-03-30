package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.TrackResults

interface TracksRepository {
    fun searchTracks(expression: String): TrackResults
}
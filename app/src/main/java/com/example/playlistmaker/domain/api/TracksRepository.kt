package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TrackResults

interface TracksRepository {
    fun searchTracks(expression: String): TrackResults
}
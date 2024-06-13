package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.ResponseStatus
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>, ResponseStatus>>
}
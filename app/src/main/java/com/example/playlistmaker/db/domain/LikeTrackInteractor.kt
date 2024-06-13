package com.example.playlistmaker.db.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface LikeTrackInteractor {
    fun likeTrackList(): Flow<List<Track>>
    suspend fun addLikeTrack(track: Track)
    suspend fun deleteLikeTrack(track: Track)
}
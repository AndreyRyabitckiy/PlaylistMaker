package com.example.playlistmaker.db.domain.impl

import com.example.playlistmaker.db.domain.LikeTrackInteractor
import com.example.playlistmaker.db.domain.LikeTrackRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LikeTrackInteractorImpl(private val likeTrackRepository: LikeTrackRepository) :
    LikeTrackInteractor {
    override fun likeTrackList(): Flow<List<Track>> {
        return likeTrackRepository.likeTrackList().map { trackList ->
            trackList.reversed()
        }
    }

    override suspend fun addLikeTrack(track: Track) {
        likeTrackRepository.addLikeTrack(track)
    }

    override suspend fun deleteLikeTrack(track: Track) {
        likeTrackRepository.deleteLikeTrack(track)
    }
}
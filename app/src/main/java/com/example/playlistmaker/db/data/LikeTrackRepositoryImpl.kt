package com.example.playlistmaker.db.data

import com.example.playlistmaker.db.data.converters.TrackDbConvertor
import com.example.playlistmaker.db.data.entity.TrackEntity
import com.example.playlistmaker.db.domain.LikeTrackRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LikeTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : LikeTrackRepository {
    override suspend fun addLikeTrack(track: Track) {
        appDatabase.trackDao().insertTracks(converterIntoEntity(track))
    }

    override suspend fun deleteLikeTrack(track: Track) {
        appDatabase.trackDao().deleteTracksEntity(converterIntoEntity(track))
    }

    override fun likeTrackList(): Flow<List<Track>> = flow {
        emit(converterIntoListTrack(appDatabase.trackDao().getLikedTracks()))
    }

    private fun converterIntoListTrack(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { trackEntity -> trackDbConvertor.map(trackEntity) }
    }

    private fun converterIntoEntity(track: Track): TrackEntity {
        return trackDbConvertor.map(track)
    }
}
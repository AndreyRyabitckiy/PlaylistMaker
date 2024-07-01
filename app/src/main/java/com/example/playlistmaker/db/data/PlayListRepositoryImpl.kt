package com.example.playlistmaker.db.data

import com.example.playlistmaker.db.data.converters.PlayListDbConvertor
import com.example.playlistmaker.db.data.dto.PlaylistTrackDto
import com.example.playlistmaker.db.data.entity.PlayListEntity
import com.example.playlistmaker.db.data.entity.PlaylistTrackEntity
import com.example.playlistmaker.db.domain.PlayListRepository
import com.example.playlistmaker.playlist_create.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListRepositoryImpl(
    private val database: AppDatabase,
    private val playListDbConvertor: PlayListDbConvertor,
) : PlayListRepository {
    override suspend fun newPlayList(playList: PlayList) =
        database.playListDao().createPlaylist(playListDbConvertor.map(playList))

    override fun listPlayList(): Flow<List<PlayList>> = flow {
        emit(convertIntoPlayList(database.playListDao().getPlaylists()))
    }

    override suspend fun getCountTracks(id: Long): Int {
        return database.playListDao().getTracksCount(id)
    }

    override suspend fun updatePlayList(track: Track, id: Long): Boolean {
        val trackAlreadyExists =
            database.playListDao().findTrack(trackId = track.trackId, playlistId = id).isNotEmpty()
        if (!trackAlreadyExists) {
            database.playListDao().addTracksToPlaylist(
                PlaylistTrackEntity(
                    trackId = track.trackId,
                    playlistId = id,
                    trackName = track.trackName,
                    artistName = track.artistName,
                    trackTimeMillis = track.trackTimeMillis,
                    artworkUrl100 = track.artworkUrl100,
                    collectionName = track.collectionName,
                    releaseDate = track.releaseDate,
                    primaryGenreName = track.primaryGenreName,
                    country = track.country,
                    previewUrl = track.previewUrl
                )
            )
        }
        return !trackAlreadyExists
    }

    private fun convertIntoPlayList(playList: List<PlayListEntity>): List<PlayList> {
        return playList.map { playListEntity -> playListDbConvertor.map(playListEntity) }
    }
}
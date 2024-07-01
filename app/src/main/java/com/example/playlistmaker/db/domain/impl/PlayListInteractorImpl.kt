package com.example.playlistmaker.db.domain.impl

import com.example.playlistmaker.db.domain.PlayListInteractor
import com.example.playlistmaker.db.domain.PlayListRepository
import com.example.playlistmaker.playlist_create.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayListInteractorImpl(private val playListRepository: PlayListRepository) :
    PlayListInteractor {
    override suspend fun newPlayList(playList: PlayList) =
        playListRepository.newPlayList(playList)


    override fun listPlayList(): Flow<List<PlayList>> {
        return playListRepository.listPlayList().map { playList ->
            playList.reversed()
        }
    }

    override suspend fun updatePlayList(track: Track, id: Long) =
        playListRepository.updatePlayList(
            Track(
                track.trackName,
                track.artistName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.trackId,
                track.collectionName,
                track.releaseDate,
                track.primaryGenreName,
                track.country,
                track.previewUrl,
                isLiked = false
            ), id
        )

    override suspend fun getCountTracks(id: Long) =
        playListRepository.getCountTracks(id)

}
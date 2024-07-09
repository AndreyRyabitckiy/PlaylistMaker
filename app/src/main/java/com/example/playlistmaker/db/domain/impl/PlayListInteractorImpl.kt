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

    override fun tracksInPlayList(id: Long): Flow<List<Track>> {
        return playListRepository.listTrackPlaylist(id).map {
            it.reversed()
        }
    }

    override suspend fun getTimesTracks(id: Long): String {
        return formatMinutes(playListRepository.getTimesTracks(id).toInt())
    }

    override suspend fun deleteTrack(id: Long) {
        playListRepository.deleteTrack(id)
    }

    override suspend fun deletePlayList(id: Long) {
        playListRepository.deletePlaylist(id)
    }

    override suspend fun shareTracks(id: Long) {
        playListRepository.shareTracks(id)
    }

    override suspend fun editPlayList(playList: PlayList) {
        playListRepository.editPlayList(playList)
    }

    override suspend fun getPlayList(id: Long): PlayList {
        return playListRepository.getPlayList(id)
    }

    private fun formatMinutes(numberMinutes: Int): String {
        if (numberMinutes % 10 == 0) {
            return "$numberMinutes минут"
        }
        if (numberMinutes % 10 == 1 && !(numberMinutes % 100 >= 11 && numberMinutes % 100 <= 19)) {
            return "$numberMinutes минута"
        }
        if (numberMinutes % 10 < 5 && !(numberMinutes % 100 >= 11 && numberMinutes % 100 <= 19)) {
            return "$numberMinutes минуты"
        } else {
            return "$numberMinutes минут"
        }
    }

}
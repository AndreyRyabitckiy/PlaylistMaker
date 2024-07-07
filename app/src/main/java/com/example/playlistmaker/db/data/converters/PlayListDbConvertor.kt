package com.example.playlistmaker.db.data.converters

import com.example.playlistmaker.db.data.entity.PlayListEntity
import com.example.playlistmaker.db.data.entity.PlaylistTrackEntity
import com.example.playlistmaker.playlist_create.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track

class PlayListDbConvertor {
    fun map(playList: PlayListEntity): PlayList {
        return PlayList(
            playList.idPlayList,
            playList.namePlayList,
            playList.aboutPlayList,
            playList.roadToFileImage
        )
    }

    fun map(playList: PlayList) =
        PlayListEntity(
            namePlayList = playList.namePlayList,
            aboutPlayList = playList.aboutPlayList,
            roadToFileImage = playList.roadToFileImage
        )

    fun map(playListTrackEntity: List<PlaylistTrackEntity>): List<Track> {
        return playListTrackEntity.map { playListTrack ->
            Track(
                trackName = playListTrack.trackName,
                artistName = playListTrack.artistName,
                trackTimeMillis = playListTrack.trackTimeMillis,
                artworkUrl100 = playListTrack.artworkUrl100,
                trackId = playListTrack.trackId,
                collectionName = playListTrack.collectionName,
                releaseDate = playListTrack.releaseDate,
                primaryGenreName = playListTrack.primaryGenreName,
                country = playListTrack.country,
                previewUrl = playListTrack.previewUrl,
                isLiked = false,
                id = playListTrack.id
            )
        }
    }
}
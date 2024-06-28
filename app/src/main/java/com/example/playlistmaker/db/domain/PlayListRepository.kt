package com.example.playlistmaker.db.domain

import com.example.playlistmaker.playlist_create.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {
    suspend fun newPlayList(playList: PlayList): Long

    fun listPlayList(): Flow<List<PlayList>>

    suspend fun updatePlayList(track: Track, id: Long): Boolean

    suspend fun getCountTracks(id: Long): Int

}
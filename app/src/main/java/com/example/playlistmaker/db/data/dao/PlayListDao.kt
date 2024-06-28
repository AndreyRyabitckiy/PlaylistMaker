package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.db.data.entity.PlayListEntity
import com.example.playlistmaker.db.data.entity.PlaylistTrackEntity

@Dao
interface PlayListDao {
    @Query("SELECT * FROM `playlist`;")
    suspend fun getPlaylists(): List<PlayListEntity>

    @Query("SELECT COUNT(*) FROM `playlist_track` WHERE `playlist_track`.`playlistId` = :id;")
    suspend fun getTracksCount(id: Long): Int

    @Query("SELECT * FROM `playlist_track` WHERE `playlist_track`.`trackId` = :trackId AND `playlist_track`.`playlistId` = :playlistId")
    suspend fun findTrack(trackId: String, playlistId: Long): List<PlaylistTrackEntity>

    @Insert
    suspend fun createPlaylist(playlist: PlayListEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTracksToPlaylist(tracks: PlaylistTrackEntity)

}
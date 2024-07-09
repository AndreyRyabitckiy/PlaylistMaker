package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.db.data.entity.PlayListEntity
import com.example.playlistmaker.db.data.entity.PlaylistTrackEntity

@Dao
interface PlayListDao {
    @Query("SELECT * FROM `playlist` WHERE `idPlayList` =:id")
    suspend fun getPlayList(id: Long): PlayListEntity

    @Query("SELECT * FROM `playlist`;")
    suspend fun getPlaylists(): List<PlayListEntity>

    @Query("SELECT COUNT(*) FROM `playlist_track` WHERE `playlist_track`.`playlistId` = :id;")
    suspend fun getTracksCount(id: Long): Int

    @Query("SELECT * FROM `playlist_track` WHERE `playlist_track`.`trackId` = :trackId AND `playlist_track`.`playlistId` = :playlistId")
    suspend fun findTrack(trackId: String, playlistId: Long): List<PlaylistTrackEntity>

    @Query("SELECT * FROM `playlist_track` WHERE `playlist_track`.playlistId =:id")
    suspend fun tracksInPlayList(id: Long): List<PlaylistTrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlaylist(playlist: PlayListEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTracksToPlaylist(tracks: PlaylistTrackEntity)

    @Query("DELETE FROM `playlist_track` WHERE id=:trackId")
    suspend fun deleteTrack(trackId: Long)

    @Query("DELETE FROM `playlist_track` WHERE playlistId=:id")
    suspend fun deletePlaylistTracks(id: Long)

    @Query("DELETE FROM `playlist` WHERE idPlayList=:id")
    suspend fun deletePlayList(id: Long)

}
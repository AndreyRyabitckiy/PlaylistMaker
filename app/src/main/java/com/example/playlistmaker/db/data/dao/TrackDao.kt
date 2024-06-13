package com.example.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.db.data.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(trackEntity: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTracksEntity(trackEntity: TrackEntity)

    @Query("SELECT * FROM `track_table`")
    suspend fun getLikedTracks(): List<TrackEntity>

    @Query("SELECT trackId From track_table;")
    suspend fun getTracksIdList(): List<String>

    @Query("SELECT COUNT(`track_table`.`trackId`) FROM `track_table` WHERE `track_table`.`trackId` = :id;")
    suspend fun hasLike(id: String): Int
}
package com.example.playlistmaker.db.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.db.data.dao.PlayListDao
import com.example.playlistmaker.db.data.dao.TrackDao
import com.example.playlistmaker.db.data.entity.PlayListEntity
import com.example.playlistmaker.db.data.entity.PlaylistTrackEntity
import com.example.playlistmaker.db.data.entity.TrackEntity

@Database(
    version = 7,
    entities = [
        TrackEntity::class,
        PlayListEntity::class,
        PlaylistTrackEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao

    abstract fun playListDao(): PlayListDao
}
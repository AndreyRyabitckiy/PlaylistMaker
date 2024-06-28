package com.example.playlistmaker.db.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_track")
data class PlaylistTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val trackId: String,
    val playlistId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
)

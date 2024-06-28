package com.example.playlistmaker.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val idPlayList: Long = 0,
    val namePlayList: String,
    val aboutPlayList: String?,
    val roadToFileImage: String
)
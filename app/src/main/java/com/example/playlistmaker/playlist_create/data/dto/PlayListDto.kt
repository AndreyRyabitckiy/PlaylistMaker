package com.example.playlistmaker.playlist_create.data.dto

data class PlayListDto(
    val id: Long = 0,
    val namePlayList: String,
    val aboutPlayList: String?,
    val roadToFileImage: String,
    val count: Int = 0,
)
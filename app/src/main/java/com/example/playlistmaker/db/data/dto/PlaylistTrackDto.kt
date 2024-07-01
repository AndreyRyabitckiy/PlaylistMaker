package com.example.playlistmaker.db.data.dto

data class PlaylistTrackDto (
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
    val previewUrl: String?
)
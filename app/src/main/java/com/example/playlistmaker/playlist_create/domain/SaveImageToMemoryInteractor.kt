package com.example.playlistmaker.playlist_create.domain

interface SaveImageToMemoryInteractor {
    suspend fun saveImageToFile(uri: String): String
}
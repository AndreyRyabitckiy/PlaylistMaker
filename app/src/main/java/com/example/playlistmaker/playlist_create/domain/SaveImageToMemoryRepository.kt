package com.example.playlistmaker.playlist_create.domain

interface SaveImageToMemoryRepository {
    suspend fun saveImageToFile(uri: String): String
}
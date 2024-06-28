package com.example.playlistmaker.playlist_create.domain

import android.net.Uri

interface SaveImageToMemoryRepository {
    suspend fun saveImageToFile(uri: Uri): String
}
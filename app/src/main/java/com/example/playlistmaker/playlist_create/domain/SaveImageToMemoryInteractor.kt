package com.example.playlistmaker.playlist_create.domain

import android.net.Uri

interface SaveImageToMemoryInteractor {
    suspend fun saveImageToFile(uri: Uri): String
}
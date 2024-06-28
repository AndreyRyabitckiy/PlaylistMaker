package com.example.playlistmaker.playlist_create.data

import android.net.Uri
import com.example.playlistmaker.playlist_create.domain.SaveImageToMemoryRepository

class SaveImageToMemoryRepositoryImpl(private val saveImageToMemory: SaveImageToMemory) :
    SaveImageToMemoryRepository {
    override suspend fun saveImageToFile(uri: Uri): String {
        return saveImageToMemory.saveImageToFile(uri)
    }
}
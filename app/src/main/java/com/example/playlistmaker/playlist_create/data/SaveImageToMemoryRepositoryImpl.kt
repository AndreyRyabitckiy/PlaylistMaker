package com.example.playlistmaker.playlist_create.data

import com.example.playlistmaker.playlist_create.domain.SaveImageToMemoryRepository

class SaveImageToMemoryRepositoryImpl(private val saveImageToMemory: SaveImageToMemory) :
    SaveImageToMemoryRepository {
    override suspend fun saveImageToFile(uri: String): String {
        return saveImageToMemory.saveImageToFile(uri)
    }
}
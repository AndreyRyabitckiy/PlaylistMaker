package com.example.playlistmaker.playlist_create.domain.impl

import android.net.Uri
import com.example.playlistmaker.playlist_create.domain.SaveImageToMemoryInteractor
import com.example.playlistmaker.playlist_create.domain.SaveImageToMemoryRepository

class SaveImageToMemoryInteractorImpl(private val repository: SaveImageToMemoryRepository) :
    SaveImageToMemoryInteractor {
    override suspend fun saveImageToFile(uri: Uri): String {
        return repository.saveImageToFile(uri)
    }
}
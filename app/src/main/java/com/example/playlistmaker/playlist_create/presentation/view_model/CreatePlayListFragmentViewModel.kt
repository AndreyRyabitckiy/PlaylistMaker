package com.example.playlistmaker.playlist_create.presentation.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.PlayListInteractor
import com.example.playlistmaker.playlist_create.domain.SaveImageToMemoryInteractor
import com.example.playlistmaker.playlist_create.domain.models.PlayList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CreatePlayListFragmentViewModel(
    private val playListInteractor: PlayListInteractor,
    private val saveImageInteractor: SaveImageToMemoryInteractor
) : ViewModel() {

    fun createNewPlayList(name: String, about: String, road: Uri?) {
        viewModelScope.launch {
            val id = playListInteractor.newPlayList(
                PlayList(
                    namePlayList = name,
                    aboutPlayList = about,
                    roadToFileImage = saveImageToFile(road)
                )
            )
        }
    }

    private val _nameImage = MutableLiveData<String>()
    val nameImage: LiveData<String>
        get() = _nameImage

    fun saveImageToFile(uri: Uri?): String {
        var road: String = ""
        if (uri != null) {
            runBlocking { road = saveImageInteractor.saveImageToFile(uri.toString()) }
        }
        return road
    }
}
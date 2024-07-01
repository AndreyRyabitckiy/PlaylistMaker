package com.example.playlistmaker.media.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.PlayListInteractor
import com.example.playlistmaker.playlist_create.domain.models.PlayList
import kotlinx.coroutines.launch

class PlayListViewModel(private val playlistInteractor: PlayListInteractor) : ViewModel() {
    private val _playList = MutableLiveData<List<PlayList>>()
    val playLists: LiveData<List<PlayList>>
        get() = _playList

    fun update() {
        viewModelScope.launch {
            playlistInteractor.listPlayList().collect {
                _playList.postValue(
                    it.map { playlist ->
                        playlist.copy(count = playlistInteractor.getCountTracks(playlist.id))
                    }
                )
            }
        }
    }
}
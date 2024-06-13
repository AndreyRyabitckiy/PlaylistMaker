package com.example.playlistmaker.media.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.LikeTrackInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class LikeMusicViewModel(private val likeTrackInteractor: LikeTrackInteractor) : ViewModel() {
    private val _tracks = MutableLiveData<List<Track>>()
    val tracksLiked: LiveData<List<Track>>
        get() = _tracks

    fun update() {
        viewModelScope.launch {
            likeTrackInteractor.likeTrackList().collect {
                _tracks.postValue(it)
            }
        }
    }
}
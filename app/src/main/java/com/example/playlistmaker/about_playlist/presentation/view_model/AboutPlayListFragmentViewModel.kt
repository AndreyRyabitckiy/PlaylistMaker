package com.example.playlistmaker.about_playlist.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.PlayListInteractor
import com.example.playlistmaker.playlist_create.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AboutPlayListFragmentViewModel(
    private val playListInteractor: PlayListInteractor,
) : ViewModel() {
    private fun convert(countTrack: Int): String {
        if (countTrack % 10 == 0) {
            return "$countTrack треков"
        }
        if (countTrack % 10 == 1 && !(countTrack % 100 >= 11 && countTrack % 100 <= 19)) {
            return "$countTrack трек"
        }
        if (countTrack % 10 < 5 && !(countTrack % 100 >= 11 && countTrack % 100 <= 19)) {
            return "$countTrack трека"
        } else {
            return "$countTrack треков"
        }
    }


    private val _aboutPlayListState = MutableLiveData<AboutPlaylistState>()
    val aboutPlayListState: LiveData<AboutPlaylistState>
        get() = _aboutPlayListState

    fun update(id: Long) {
        viewModelScope.launch {
            val tracks = ArrayList<Track>()
            var time = playListInteractor.getTimesTracks(id)
            val count = convert(playListInteractor.getCountTracks(id))
            playListInteractor.tracksInPlayList(id).collect {
                tracks.addAll(it)
            }
            _aboutPlayListState.postValue(AboutPlaylistState(tracks, time, count))
        }
    }

    private val _playlist = MutableLiveData<PlayList>()
    val playList: LiveData<PlayList>
        get() = _playlist

    fun updatePlayList(id: Long) {
        viewModelScope.launch {
            _playlist.postValue(playListInteractor.getPlayList(id))
        }
    }

    fun deleteTrack(trackId: Long, playListId: Long) {
        viewModelScope.launch {
            playListInteractor.deleteTrack(trackId)
            update(playListId)
        }
    }

    fun shareTracks(id: Long) {
        viewModelScope.launch {
            playListInteractor.shareTracks(id)
        }
    }

    fun deletePlaylist(id: Long) {
        runBlocking {
            playListInteractor.deletePlayList(id)
        }
    }
}

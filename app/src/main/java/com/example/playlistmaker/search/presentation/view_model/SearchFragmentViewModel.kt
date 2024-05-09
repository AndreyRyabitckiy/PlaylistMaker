package com.example.playlistmaker.search.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.ResponseStatus
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.sharedprefs.SharedPrefsInteractor

class SearchFragmentViewModel(
    private val sharedPrefsInteractor: SharedPrefsInteractor,
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>>
        get() = _tracks
    private val _searchStatus = MutableLiveData<ResponseStatus>()
    val searchStatus: LiveData<ResponseStatus>
        get() = _searchStatus


    fun searchTracks(trackName: String) {
        tracksInteractor.searchTracks(
            trackName,
            consumer = object : TracksInteractor.TracksConsumer {
                override fun consume(foundTrack: List<Track>, status: ResponseStatus) {
                    _searchStatus.postValue(status)
                    _tracks.postValue(foundTrack)
                }
            })
    }

    private val _tracksHistory = MutableLiveData<ArrayList<Track>>()
    val tracksHistory: LiveData<ArrayList<Track>>
        get() = _tracksHistory

    fun sharedPrefsWork(uses: String, track: Track? = null) {
        sharedPrefsInteractor.readWriteClear(
            uses,
            track,
            consumer = object : SharedPrefsInteractor.SharedPrefsConsumer {
                override fun consume(foundSharedPrefs: ArrayList<Track>) {
                    _tracksHistory.postValue(foundSharedPrefs)
                }
            })
    }
}
package com.example.playlistmaker.search.presentation.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.ResponseStatus
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.sharedprefs.SharedPrefsInteractor

class SearchActivityViewModel(
    private val sharedPrefsInteractor: SharedPrefsInteractor
) : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>>
        get() = _tracks
    private val _searchStatus = MutableLiveData<ResponseStatus>()
    val searchStatus: LiveData<ResponseStatus>
        get() = _searchStatus
    private val interactorTracks = Creator.provideTracksInteractor()

    fun searchTracks(trackName: String) {
        interactorTracks.searchTracks(
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

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val sharedPrefs = Creator.SharedPrefsInteractor(context)
            return SearchActivityViewModel(sharedPrefsInteractor = sharedPrefs
            )
             as T
        }
    }
}
package com.example.playlistmaker.search.presentation.view_model

import android.os.Handler
import android.os.Looper
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

    private val searchRunnable = Runnable {
        if (searchText.isNotBlank()) {
            searchTracks(searchText)
        }
    }
    private val handler = Handler(Looper.getMainLooper())

    fun clickDebounce() {
        _isClickAllowed.value?.let { isAllowed ->
            if (isAllowed) {
                _isClickAllowed.postValue(false)
                handler.postDelayed(
                    {
                        _isClickAllowed.postValue(true)
                    },
                    CLICK_DEBOUNCE_DELAY
                )
            }
        }
    }
    private val _isClickAllowed = MutableLiveData<Boolean>()
    val isClickAllowed: LiveData<Boolean>
        get() = _isClickAllowed

    private var searchText = ""

    private val historyTracks: List<Track>
        get() = getHistory()

    private val _showHistory = MutableLiveData<Boolean>(false)
    val showHistory:LiveData<Boolean>
        get() = _showHistory
    fun showHistoryBoolean(answer:Boolean, rewrite: Boolean = true) {
        if (answer) {
            if (historyTracks.isNotEmpty()) {
                _tracks.postValue(historyTracks)
                _showHistory.postValue(answer)
            } else {
                _showHistory.postValue(false)
            }
        } else if (rewrite){
            _showHistory.postValue(false)
            _tracks.postValue(emptyList())
        }
    }

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>>
        get() = _tracks
    private val _searchStatus = MutableLiveData<ResponseStatus>()
    val searchStatus: LiveData<ResponseStatus>
        get() = _searchStatus

    fun setSearchText(text: String) {
        searchText = text
    }
    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun searchTracks(trackName: String) {
        _searchStatus.postValue(ResponseStatus.LOADING)
        tracksInteractor.searchTracks(
            trackName,
            consumer = object : TracksInteractor.TracksConsumer {
                override fun consume(foundTrack: List<Track>, status: ResponseStatus) {
                    _searchStatus.postValue(status)
                    _tracks.postValue(foundTrack)
                    if (status == ResponseStatus.SUCCESS) {
                        _showHistory.postValue(false)
                    }
                }
            })
    }

    private fun getHistory() = sharedPrefsInteractor.readWriteClearWithoutConsumer(USE_READ, null)

    fun writeHistory(track: Track?) {
        sharedPrefsWork(USE_WRITE, track)
    }

    fun clearHistory() {
        sharedPrefsWork(USE_CLEAR)
        _tracks.postValue(historyTracks)
    }

    fun resume() {
        if (_showHistory.value == true) {
            _tracks.postValue(historyTracks)
        }

        _isClickAllowed.postValue(true)
    }

    private fun sharedPrefsWork(uses: String, track: Track? = null) {
        sharedPrefsInteractor.readWriteClear(
            uses,
            track,
            consumer = object : SharedPrefsInteractor.SharedPrefsConsumer {
                override fun consume(foundSharedPrefs: ArrayList<Track>) {

                }
            })
    }

    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val USE_CLEAR = "clear"
        const val USE_READ = "read"
        private const val USE_WRITE = "write"
    }
}
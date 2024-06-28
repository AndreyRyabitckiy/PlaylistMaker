package com.example.playlistmaker.player.presentation.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.db.domain.LikeTrackInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MusicPlayerViewModel(
    private val track: Track,
    private val likeTrackInteractor: LikeTrackInteractor,
    private val appDatabase: AppDatabase
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    private var _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean>
        get() = _isLiked

    private var mediaPlayer = MediaPlayer()
    private var mediaPlayerState = PlayerState.DEFAULT

    private val _timerLiveData = MutableLiveData<String>()
    private var timerJob: Job? = null
    val timerLiveData: LiveData<String>
        get() = _timerLiveData

    private val _playerState = MutableLiveData<PlayerState>()

    val playerState: LiveData<PlayerState>
        get() = _playerState

    private var playerPosition: Long = 0L
    fun getLikeStatus(id: String) = viewModelScope.launch(Dispatchers.IO) {
        val like = appDatabase.trackDao().hasLike(id) > 0
        _isLiked.postValue(like)
    }

    fun changeLikedStatus() {
        val newStatus = _isLiked.value != true
        _isLiked.postValue(newStatus)
        if (newStatus) {
            viewModelScope.launch {
                likeTrackInteractor.addLikeTrack(track)
            }
        } else {
            viewModelScope.launch {
                likeTrackInteractor.deleteLikeTrack(track)
            }
        }
    }

    fun setPlayerPosition() {
        playerPosition = mediaPlayer.currentPosition.toLong()
    }

    private fun createUpdateTimerMusicTask() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayerState == PlayerState.PLAYING) {
                delay(DELAY)
                _timerLiveData.postValue(dateFormat.format(playerPosition).toString())
            }
        }
    }

    fun startTimerMusic() {
        if (mediaPlayerState == PlayerState.PLAYING) {
            createUpdateTimerMusicTask()
        } else {
            timerJob?.cancel()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayerState = PlayerState.PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            _timerLiveData.postValue(TIME_START)
            mediaPlayerState = PlayerState.PREPARED
            _playerState.postValue(mediaPlayerState)
            timerJob?.cancel()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        mediaPlayerState = PlayerState.PLAYING
        _playerState.postValue(mediaPlayerState)

    }

    fun pausePlayer() {
        mediaPlayer.pause()
        mediaPlayerState = PlayerState.PAUSED
        _playerState.postValue(mediaPlayerState)
    }

    fun mediaPlayerRelease() {
        mediaPlayer.release()
    }

    fun playbackControl() {
        when (mediaPlayerState) {
            PlayerState.PLAYING -> {
                pausePlayer()
            }

            PlayerState.PREPARED, PlayerState.PAUSED -> {
                startPlayer()
            }

            else -> Unit
        }
    }

    companion object {
        private const val TIME_START = "00:00"
        private const val DELAY = 300L
    }
}
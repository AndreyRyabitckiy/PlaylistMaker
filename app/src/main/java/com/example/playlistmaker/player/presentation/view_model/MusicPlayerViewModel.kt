package com.example.playlistmaker.player.presentation.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class MusicPlayerViewModel : ViewModel() {

    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    private var mediaPlayer = MediaPlayer()
    private var mediaPlayerState = PlayerState.DEFAULT

    private val _timerLiveData = MutableLiveData<String>()
    val timerLiveData: LiveData<String>
        get() = _timerLiveData

    private val _playerState = MutableLiveData<PlayerState>()

    val playerState: LiveData<PlayerState>
        get() = _playerState

    private val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }

    private var playerPosition: Long = 0L

    fun setPlayerPosition() {
        playerPosition = mediaPlayer.currentPosition.toLong()
    }

    private fun createUpdateTimerMusicTask(): Runnable {
        return object : Runnable {
            override fun run() {

                if (playerPosition < MUSIC_TIME) {
                    _timerLiveData.postValue(dateFormat.format(playerPosition).toString())
                    mainThreadHandler.postDelayed(this, DELAY)
                } else {
                    _timerLiveData.postValue(TIME_START)
                    mainThreadHandler.postDelayed(this, DELAY)
                }
            }
        }
    }

    fun startTimerMusic() {
        if (mediaPlayerState == PlayerState.PREPARED || mediaPlayerState == PlayerState.PAUSED)
            mainThreadHandler.post(
                createUpdateTimerMusicTask()
            ) else {
            mainThreadHandler.removeCallbacksAndMessages(null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayerState = PlayerState.PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            _playerState.postValue(mediaPlayerState)
            mediaPlayerState = PlayerState.PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        _playerState.postValue(mediaPlayerState)
        mediaPlayerState = PlayerState.PLAYING
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        _playerState.postValue(mediaPlayerState)
        mediaPlayerState = PlayerState.PAUSED
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
        private const val MUSIC_TIME = 29900
        private const val TIME_START = "00:00"
        private const val DELAY = 300L
    }
}
package com.example.playlistmaker.presentation.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class MusicPlayerViewModel : ViewModel() {

    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val MUSIC_TIME = 29900
    private val TIME_START = "00:00"
    private val DELAY = 300L

    private val _timerLiveData = MutableLiveData<String>()
    val timerLiveData: LiveData<String>
        get() = _timerLiveData

    private val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }

    private var playerPosition: Long = 0L

    fun setPlayerPosition(position: Int) {
        playerPosition = position.toLong()
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

    fun startTimerMusic(playerState: PlayerState) {
        if (playerState == PlayerState.PREPARED || playerState == PlayerState.PAUSED)
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
}
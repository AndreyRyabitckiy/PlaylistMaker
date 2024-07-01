package com.example.playlistmaker.player.presentation.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.db.domain.LikeTrackInteractor
import com.example.playlistmaker.db.domain.PlayListInteractor
import com.example.playlistmaker.playlist_create.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MusicPlayerViewModel(
    private val track: Track,
    private val likeTrackInteractor: LikeTrackInteractor,
    private val likeTrackDatabase: AppDatabase,
    private val playListInteractor: PlayListInteractor
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
        val like = likeTrackDatabase.trackDao().hasLike(id) > 0
        _isLiked.postValue(like)
    }

    fun changeLikedStatus() {
        val newStatus = _isLiked.value != true
        _isLiked.postValue(newStatus)
        viewModelScope.launch {
            if (newStatus) {
                likeTrackInteractor.addLikeTrack(track)
            } else {
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

    var playePrepare = false

    fun preparePlayer() {
        if (!playePrepare) {
            mediaPlayer.setDataSource(track.previewUrl.toString())
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
            playePrepare = true
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        mediaPlayerState = PlayerState.PLAYING
        _playerState.postValue(mediaPlayerState)

    }

    fun pausePlayer() {
        if (mediaPlayerState != PlayerState.PREPARED){
            mediaPlayer.pause()
            mediaPlayerState = PlayerState.PAUSED
            _playerState.postValue(mediaPlayerState)
        }
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

    private val _playList = MutableLiveData<List<PlayList>>()
    val playLists: LiveData<List<PlayList>>
        get() = _playList

    fun update() {
        viewModelScope.launch {
            playListInteractor.listPlayList().collect {
                _playList.postValue(
                    it.map { playlist ->
                        playlist.copy(count = playListInteractor.getCountTracks(playlist.id))
                    }
                )
            }
        }
    }

    private var _trackAddedFlow = MutableSharedFlow<ToastState>()
    val trackAddedFlow = _trackAddedFlow.asSharedFlow()

    fun insertInPlayList(id: Long, namePlayList:String) {
        viewModelScope.launch {
            val trackAdded = playListInteractor.updatePlayList(track, id)
            _trackAddedFlow.emit(ToastState(trackAdded, namePlayList))
            update()
        }
    }

    companion object {
        private const val TIME_START = "00:00"
        private const val DELAY = 300L
    }
}
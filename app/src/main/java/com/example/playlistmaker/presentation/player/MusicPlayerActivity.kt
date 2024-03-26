package com.example.playlistmaker.presentation.player

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.RADIUS_CUT_IMAGE
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.dpToPx
import com.example.playlistmaker.parcelable
import com.google.android.material.button.MaterialButton

class MusicPlayerActivity : AppCompatActivity() {


    private var playerState = PlayerState.DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var url: String? = " "
    private lateinit var playButton: MaterialButton
    private val timeMusic30 by lazy { findViewById<TextView>(R.id.timeMusic30Tv) }


    private val viewModel by viewModels<MusicPlayerViewModel>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        val backButton = findViewById<ImageView>(R.id.backIv)
        val musicImage = findViewById<ImageView>(R.id.musicImageIv)
        val nameMusic = findViewById<TextView>(R.id.nameMusicTv)
        val groupName = findViewById<TextView>(R.id.groupNameTv)
        val timeMusic = findViewById<TextView>(R.id.timeMusicAnswerTv)
        val groupMusic = findViewById<TextView>(R.id.groupMusicAnswerTv)
        val ear = findViewById<TextView>(R.id.earAnswerTv)
        val typeMusic = findViewById<TextView>(R.id.typeMusicAnswerTv)
        val country = findViewById<TextView>(R.id.countryAnswerTv)
        val textGroup = findViewById<Group>(R.id.textGroup)

        playButton = findViewById(R.id.playMusicB)

        backButton.setOnClickListener {
            finish()
        }

        intent.parcelable<Track>(TRACK_KEY)?.let { track ->
            url = track.previewUrl
            nameMusic.text = track.trackName
            groupName.text = track.artistName
            timeMusic.text = track.trackTimeMillis
            if (track.collectionName == null) {
                textGroup.isVisible = false
            } else {
                groupMusic.text = track.collectionName
            }
            ear.text = track.releaseDate
            typeMusic.text = track.primaryGenreName
            country.text = track.country
            val urlImage = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

            Glide.with(this)
                .load(urlImage)
                .placeholder(R.drawable.placeholder_ic)
                .centerInside()
                .transform(RoundedCorners(dpToPx(RADIUS_CUT_IMAGE, this)))
                .into(musicImage)
        }

        preparePlayer()

        playButton.setOnClickListener {
            viewModel.setPlayerPosition(mediaPlayer.currentPosition)
            viewModel.startTimerMusic(playerState)
            playbackControl()
        }

        viewModel.timerLiveData.observe(this) {
            timeMusic30.text = it
            viewModel.setPlayerPosition(mediaPlayer.currentPosition)
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            playButton.setIconResource(R.drawable.ic_play)
            playerState = PlayerState.PREPARED
        }
    }

    private fun playbackControl() {
        when (playerState) {
            PlayerState.PLAYING -> {
                pausePlayer()
            }

            PlayerState.PREPARED, PlayerState.PAUSED -> {
                startPlayer()
            }

            else -> Unit
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setIconResource(R.drawable.pause_ic)
        playerState = PlayerState.PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setIconResource(R.drawable.ic_play)
        playerState = PlayerState.PAUSED
    }

    companion object {
        const val TRACK_KEY = "TRACK"
    }
}
package com.example.playlistmaker

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Locale

class MusicPlayerActivity : AppCompatActivity() {

    companion object {
        const val TRACK_KEY = "TRACK"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 300L
        private const val MUSIC_TIME = 29900
        private const val TIME_START = "00:00"
    }

    private val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }

    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var url: String? = " "
    private lateinit var playButton: MaterialButton
    private val timeMusic30 by lazy { findViewById<TextView>(R.id.timeMusic30Tv) }

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
            timeMusic.text = dateFormat.format(track.trackTimeMillis.toLong())
            if (track.collectionName == null) {
                textGroup.isVisible = false
            } else {
                groupMusic.text = track.collectionName
            }
            ear.text = getFormattedDate(track.releaseDate)
            typeMusic.text = track.primaryGenreName
            country.text = track.country
            val urlImage = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

            Glide.with(this)
                .load(urlImage)
                .placeholder(R.drawable.placeholder_ic)
                .centerInside()
                .transform(RoundedCorners(radiusCutImage))
                .into(musicImage)
        }

        preparePlayer()
        playButton.setOnClickListener {
            startTimerMusic()
            playbackControl()
        }
    }

    private fun startTimerMusic() {
        if (playerState == STATE_PREPARED || playerState == STATE_PAUSED)
            mainThreadHandler?.post(
                createUpdateTimerMusicTask()
            ) else {
            mainThreadHandler.removeCallbacksAndMessages(null)
        }
    }

    private fun createUpdateTimerMusicTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val timeMusicAnswer = mediaPlayer.currentPosition
                if (timeMusicAnswer < MUSIC_TIME) {
                    timeMusic30.text = dateFormat.format(timeMusicAnswer.toLong()).toString()
                    mainThreadHandler?.postDelayed(this, DELAY)
                } else {
                    timeMusic30.text = TIME_START
                    mainThreadHandler?.postDelayed(this, DELAY)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        mediaPlayer.release()
        mainThreadHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    private fun getFormattedDate(inputDate: String?): String {
        inputDate ?: return ""

        val dfIn = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val dfOut = SimpleDateFormat("yyyy", Locale.getDefault())

        dfIn.parse(inputDate)?.let { date ->
            return dfOut.format(date).orEmpty() ?: ""
        } ?: return ""
    }

    @SuppressLint("SetTextI18n")
    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            playButton.setIconResource(R.drawable.ic_play)
            playerState = STATE_PREPARED
        }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setIconResource(R.drawable.pause_ic)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setIconResource(R.drawable.ic_play)
        playerState = STATE_PAUSED
    }
}



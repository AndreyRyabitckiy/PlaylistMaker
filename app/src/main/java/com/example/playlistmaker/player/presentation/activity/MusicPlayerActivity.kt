package com.example.playlistmaker.player.presentation.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.RADIUS_CUT_IMAGE
import com.example.playlistmaker.databinding.ActivityMusicPlayerBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.dpToPx
import com.example.playlistmaker.parcelable
import com.example.playlistmaker.player.presentation.view_model.MusicPlayerViewModel
import com.example.playlistmaker.player.presentation.view_model.PlayerState

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMusicPlayerBinding
    private var url: String? = " "
    private val viewModel by viewModels<MusicPlayerViewModel>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIv.setOnClickListener {
            finish()
        }

        intent.parcelable<Track>(TRACK_KEY)?.let { track ->
            url = track.previewUrl
            binding.nameMusicTv.text = track.trackName
            binding.groupNameTv.text = track.artistName
            binding.timeMusicAnswerTv.text = track.trackTimeMillis
            if (track.collectionName == null) {
                binding.textGroup.isVisible = false
            } else {
                binding.groupMusicAnswerTv.text = track.collectionName
            }
            binding.earAnswerTv.text = track.releaseDate
            binding.typeMusicAnswerTv.text = track.primaryGenreName
            binding.countryAnswerTv.text = track.country
            val urlImage = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

            Glide.with(this)
                .load(urlImage)
                .placeholder(R.drawable.placeholder_ic)
                .centerInside()
                .transform(RoundedCorners(dpToPx(RADIUS_CUT_IMAGE, this)))
                .into(binding.musicImageIv)

        }

        viewModel.preparePlayer(url.toString())


        binding.playMusicB.setOnClickListener {
            viewModel.setPlayerPosition()
            viewModel.startTimerMusic()
            viewModel.playbackControl()
        }

        viewModel.timerLiveData.observe(this) {
            binding.timeMusic30Tv.text = it
            viewModel.setPlayerPosition()
        }

        viewModel.playerState.observe(this) {
            if (it == PlayerState.PREPARED || it == PlayerState.PAUSED) {
                binding.playMusicB.setIconResource(R.drawable.pause_ic)
            } else {
                binding.playMusicB.setIconResource(R.drawable.ic_play)
            }
        }
    }

    override fun onPause() {
        viewModel.pausePlayer()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.mediaPlayerRelease()
        super.onDestroy()
    }

    companion object {
        const val TRACK_KEY = "TRACK"
    }
}
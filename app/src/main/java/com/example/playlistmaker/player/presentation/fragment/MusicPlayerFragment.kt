package com.example.playlistmaker.player.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.app.RADIUS_CUT_IMAGE
import com.example.playlistmaker.app.dpToPx
import com.example.playlistmaker.app.parcelable
import com.example.playlistmaker.databinding.FragmentMusicPlayerBinding
import com.example.playlistmaker.player.presentation.view_model.MusicPlayerViewModel
import com.example.playlistmaker.player.presentation.view_model.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class MusicPlayerFragment : Fragment() {

    private lateinit var binding: FragmentMusicPlayerBinding
    private var url: String? = " "
    private val viewModel: MusicPlayerViewModel by viewModel<MusicPlayerViewModel>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMusicPlayerBinding.inflate(inflater,container,false)
        val view = binding.root

        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }

        if (arguments != null) {
            requireArguments().parcelable<Track>(TRACK_KEY)?.let { track ->
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
                    .transform(RoundedCorners(dpToPx(RADIUS_CUT_IMAGE, requireContext())))
                    .into(binding.musicImageIv)

            }
        }

        viewModel.preparePlayer(url.toString())

        binding.playMusicB.setOnClickListener {
            viewModel.setPlayerPosition()
            viewModel.startTimerMusic()
            viewModel.playbackControl()
        }

        viewModel.timerLiveData.observe(viewLifecycleOwner) {
            binding.timeMusic30Tv.text = it
            viewModel.setPlayerPosition()
        }

        viewModel.playerState.observe(viewLifecycleOwner) {
            if (it == PlayerState.PREPARED || it == PlayerState.PAUSED) {
                binding.playMusicB.setIconResource(R.drawable.pause_ic)
            } else {
                binding.playMusicB.setIconResource(R.drawable.ic_play)
            }
        }
        return view
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

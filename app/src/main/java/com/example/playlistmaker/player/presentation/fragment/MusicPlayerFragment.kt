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

    private var _binding: FragmentMusicPlayerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MusicPlayerViewModel by viewModel<MusicPlayerViewModel>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicPlayerBinding.inflate(inflater,container,false)

        if (arguments != null) {
            trackItemUse()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.playerState.observe(viewLifecycleOwner) {playerState ->
            if (playerState == PlayerState.PREPARED || playerState == PlayerState.PAUSED) {
                binding.playMusicB.setIconResource(R.drawable.ic_play)
            } else {
                binding.playMusicB.setIconResource(R.drawable.pause_ic)
            }
        }

        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.playMusicB.setOnClickListener {
            prepareMedia()
        }

        viewModel.timerLiveData.observe(viewLifecycleOwner) {time ->
            binding.timeMusic30Tv.text = time
            viewModel.setPlayerPosition()
        }
    }

    private fun trackItemUse(){
        binding.run {
            requireArguments().parcelable<Track>(TRACK_KEY)?.let { track ->
                nameMusicTv.text = track.trackName
                groupNameTv.text = track.artistName
                timeMusicAnswerTv.text = track.trackTimeMillis
                if (track.collectionName == null) {
                    textGroup.isVisible = false
                } else {
                    groupMusicAnswerTv.text = track.collectionName
                }
                earAnswerTv.text = track.releaseDate
                typeMusicAnswerTv.text = track.primaryGenreName
                countryAnswerTv.text = track.country
                val urlImage = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

                Glide.with(requireContext())
                    .load(urlImage)
                    .placeholder(R.drawable.placeholder_ic)
                    .centerInside()
                    .transform(RoundedCorners(dpToPx(RADIUS_CUT_IMAGE, requireContext())))
                    .into(musicImageIv)

                viewModel.preparePlayer(track.previewUrl.toString())
            }
        }
    }

    private fun prepareMedia(){
        viewModel.playbackControl()
        viewModel.startTimerMusic()
        viewModel.setPlayerPosition()
    }

    override fun onPause() {
        viewModel.pausePlayer()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.mediaPlayerRelease()
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TRACK_KEY = "TRACK"
    }
}

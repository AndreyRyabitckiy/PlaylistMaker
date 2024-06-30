package com.example.playlistmaker.player.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.app.RADIUS_CUT_IMAGE_PLAYER
import com.example.playlistmaker.app.dpToPx
import com.example.playlistmaker.app.parcelable
import com.example.playlistmaker.app.showCustomToast
import com.example.playlistmaker.databinding.FragmentMusicPlayerBinding
import com.example.playlistmaker.player.presentation.view_model.MusicPlayerViewModel
import com.example.playlistmaker.player.presentation.view_model.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MusicPlayerFragment : Fragment() {

    private val adapter by lazy { BottomSheetPlayListAdapter() }
    private var _binding: FragmentMusicPlayerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MusicPlayerViewModel by viewModel<MusicPlayerViewModel> {
        parametersOf(
            requireArguments().parcelable<Track>(TRACK_KEY)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicPlayerBinding.inflate(inflater, container, false)

        if (arguments != null) {
            trackItemUse()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.preparePlayer()

        viewModel.trackInPlayList.observe(viewLifecycleOwner) { toastState ->
            makeToast(toastState.answer, toastState.name)
        }

        binding.recyclerViewBS.adapter = adapter

        viewModel.playLists.observe(viewLifecycleOwner) {
            adapter.data = it
        }

        binding.bsbtNewPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            findNavController().navigate(
                R.id.action_musicPlayerFragment_to_createPlayList
            )
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                stateBottomSheet(newState)
            }

            override fun onSlide(p0: View, p1: Float) {}
        })

        adapter.onClick = { playList ->
            viewModel.insertInPlayList(playList.id, playList.namePlayList)
            viewModel.update()
        }

        binding.playlistAddB.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewModel.playerState.observe(viewLifecycleOwner) { playerState ->
            playerState(playerState)
        }

        viewModel.isLiked.observe(viewLifecycleOwner) { isLiked -> isLike(isLiked) }

        binding.likeMusicB.setOnClickListener {
            viewModel.changeLikedStatus()
        }

        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.playMusicB.setOnClickListener { prepareMedia() }

        viewModel.timerLiveData.observe(viewLifecycleOwner) { time ->
            binding.timeMusic30Tv.text = time
            viewModel.setPlayerPosition()
        }
    }

    private fun makeToast(answer: Boolean, name: String) {
        if (answer) {
            Toast(requireContext()).showCustomToast(getString(R.string.add_playlist_yes, name), requireActivity())
        } else {
            Toast(requireContext()).showCustomToast(getString(R.string.add_playlist_no, name), requireActivity())
        }
    }

    private fun playerState(playerState: PlayerState) {
        if (playerState == PlayerState.PREPARED || playerState == PlayerState.PAUSED) {
            binding.playMusicB.setIconResource(R.drawable.ic_play)
        } else {
            binding.playMusicB.setIconResource(R.drawable.pause_ic)
        }
    }

    private fun isLike(answer: Boolean) {
        if (answer) {
            binding.likeMusicB.setIconResource(R.drawable.like_ic_red)
            binding.likeMusicB.setIconTintResource(R.color.red_like)
        } else {
            binding.likeMusicB.setIconResource(R.drawable.like_ic)
            binding.likeMusicB.setIconTintResource(R.color.white)
        }
    }

    private fun stateBottomSheet(newState: Int) {
        when (newState) {
            BottomSheetBehavior.STATE_HIDDEN -> {
                binding.overlay.visibility = View.GONE
            } else -> {
                binding.overlay.visibility = View.VISIBLE
            }
        }
    }

    private fun trackItemUse() {
        binding.run {
            requireArguments().parcelable<Track>(TRACK_KEY)?.let { track ->
                viewModel.getLikeStatus(track.trackId)
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
                    .transform(RoundedCorners(dpToPx(RADIUS_CUT_IMAGE_PLAYER, requireContext())))
                    .into(musicImageIv)


            }
        }
    }

    private fun prepareMedia() {
        viewModel.playbackControl()
        viewModel.startTimerMusic()
        viewModel.setPlayerPosition()
    }

    override fun onPause() {
        viewModel.pausePlayer()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        viewModel.update()
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

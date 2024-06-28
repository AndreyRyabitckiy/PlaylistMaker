package com.example.playlistmaker.media.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLikeMusicBinding
import com.example.playlistmaker.media.presentation.view_model.LikeMusicViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.fragment.TrackAdapter
import com.example.playlistmaker.ui.root.RootActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LikeMusicFragment : Fragment() {

    private val viewModel: LikeMusicViewModel by viewModel()
    private var _binding: FragmentLikeMusicBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { TrackAdapter() }
    private var debounceClick = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLikeMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rwTrackLiked.adapter = adapter
        viewModel.tracksLiked.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.rwTrackLiked.isVisible = false
                binding.tvViewHolder.isVisible = true
                binding.flViewHolder.isVisible = true
                adapter.data = it
            } else {
                binding.rwTrackLiked.isVisible = true
                binding.tvViewHolder.isVisible = false
                binding.flViewHolder.isVisible = false
                adapter.data = it
            }
        }

        viewModel.update()
        adapter.onClick = { item ->
            (activity as RootActivity).animateBottomNavigationViewFalse()
            onClickAdapter(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClickAdapter(track: Track) {
        if (debounceClick) {
            debounceClick = false
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_musicPlayerFragment,
                bundleOf(TRACK_KEY to track)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        debounceClick = true
        (activity as RootActivity).animateBottomNavigationViewTrue()
    }

    companion object {
        private const val LIKE_MUSIC_NUMBER = "like_music_number"
        private const val TRACK_KEY = "TRACK"

        fun newInstance() = LikeMusicFragment().apply {
            arguments = Bundle().apply {
                putInt(LIKE_MUSIC_NUMBER, 1)
            }
        }
    }
}
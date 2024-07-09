package com.example.playlistmaker.media.presentation.fragment.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.about_playlist.presentation.fragment.AboutPlayListFragment
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.presentation.view_model.PlayListViewModel
import com.example.playlistmaker.ui.root.RootActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private val viewModel: PlayListViewModel by viewModel()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { PlayListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        viewModel.playLists.observe(viewLifecycleOwner) {
            with(binding) {
                if (it.isEmpty()) {
                    recyclerView.isVisible = false
                    flPlaceHolder.isVisible = true
                    tvPlaceHolder.isVisible = true
                } else {
                    recyclerView.isVisible = true
                    flPlaceHolder.isVisible = false
                    tvPlaceHolder.isVisible = false
                    adapter.data = it
                }
            }
        }

        adapter.onClick = { playList ->
            (activity as RootActivity).animateBottomNavigationViewFalse()
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_aboutPlayListFragment,
                bundleOf(PLAYLIST_ITEM to playList)
            )
        }

        binding.btNewPlaylist.setOnClickListener {
            (activity as RootActivity).animateBottomNavigationViewFalse()
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_createPlayList
            )
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.update()
        (activity as RootActivity).animateBottomNavigationViewTrue()
    }

    companion object {
        private const val PLAYLIST_NUMBER = "playlist_number"
        private const val PLAYLIST_ITEM = "Playlist"

        fun newInstance() = PlaylistFragment().apply {
            arguments = Bundle().apply {
                putInt(PLAYLIST_NUMBER, 1)
            }
        }
    }
}
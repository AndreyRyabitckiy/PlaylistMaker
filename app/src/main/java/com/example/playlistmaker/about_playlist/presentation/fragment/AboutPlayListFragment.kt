package com.example.playlistmaker.about_playlist.presentation.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.about_playlist.presentation.view_model.AboutPlayListFragmentViewModel
import com.example.playlistmaker.app.parcelable
import com.example.playlistmaker.app.showCustomToast
import com.example.playlistmaker.databinding.FragmentAboutPlaylistBinding
import com.example.playlistmaker.playlist_create.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.fragment.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutPlayListFragment : Fragment() {

    private var _binding: FragmentAboutPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AboutPlayListFragmentViewModel by viewModel()
    private var imageUri: Uri? = null
    private val adapter by lazy { TrackAdapter() }
    private var debounceClick = true
    lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutPlaylistBinding.inflate(inflater, container, false)

        if (arguments != null) {
            playlistItemUse()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewBS.adapter = adapter

        binding.editPlayList.setOnClickListener {
            findNavController().navigate(
                R.id.action_aboutPlayListFragment_to_createPlayList,
                bundleOf(
                    PLAYLIST_ITEM to PlayList(
                        requireArguments().parcelable<PlayList>(PLAYLIST_ITEM)!!.id,
                        binding.tvNamePlaylist.text.toString(),
                        binding.tvAboutPlaylist.text.toString(),
                        imageUri.toString()
                    )
                )
            )
        }

        binding.bShare.setOnClickListener { shareTracks() }

        binding.shareButton.setOnClickListener { shareTracks() }

        binding.bDeletePlayList.setOnClickListener {
            deletePlaylist(
                requireArguments().parcelable<PlayList>(PLAYLIST_ITEM)!!.id,
                requireArguments().parcelable<PlayList>(PLAYLIST_ITEM)!!.namePlayList
            )
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetMore).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.bMore.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                stateBottomSheet(newState)
            }

            override fun onSlide(p0: View, p1: Float) {}
        })

        viewModel.playList.observe(viewLifecycleOwner) { playlist ->
            arguments
            with(binding) {
                tvNamePlaylistBS.text = playlist.namePlayList
                tvNamePlaylist.text = playlist.namePlayList
                tvAboutPlaylist.text = playlist.aboutPlayList
                imageUri = playlist.roadToFileImage.toUri()
                if (playlist.roadToFileImage.isNotEmpty()) {
                    ivImageBS.setImageURI(playlist.roadToFileImage.toUri())
                    ivImage.setImageURI(playlist.roadToFileImage.toUri())
                } else {
                    ivImageBS.setImageResource(R.drawable.placeholder_ic)
                    ivImage.setImageResource(R.drawable.placeholder_ic)
                }
            }
        }

        viewModel.aboutPlayListState.observe(viewLifecycleOwner) {
            binding.tvCountTrack.text = it.count
            binding.tvTimeTracks.text = it.time
            adapter.data = it.tracks
            binding.tvCountTrackBS.text = it.count
        }

        adapter.onClick = { track ->
            if (debounceClick) {
                debounceClick = false
                findNavController().navigate(
                    R.id.action_aboutPlayListFragment_to_musicPlayerFragment,
                    bundleOf(TRACK_KEY to track)
                )
            }
        }

        adapter.onClickLong = {
            openDialog(it)
        }

        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun shareTracks() {
        if (adapter.data.isNotEmpty()) {
            viewModel.shareTracks(requireArguments().parcelable<PlayList>(PLAYLIST_ITEM)!!.id)
        } else {
            Toast(requireContext()).showCustomToast(
                getString(R.string.not_tracks_share),
                requireActivity()
            )
        }
    }

    private fun stateBottomSheet(newState: Int) {
        when (newState) {
            BottomSheetBehavior.STATE_HIDDEN -> {
                binding.overlay.visibility = View.GONE
            }

            else -> {
                binding.overlay.visibility = View.VISIBLE
            }
        }
    }


    private fun playlistItemUse() {
        binding.run {
            requireArguments().parcelable<PlayList>(PLAYLIST_ITEM)?.let { playlist ->
                imageUri = playlist.roadToFileImage.toUri()
                viewModel.update(playlist.id)
            }
        }
    }

    private fun openDialog(track: Track) {
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setMessage("Хотите удалить трек?")
            .setNegativeButton("Нет") { dialog, which -> }
            .setPositiveButton("Да") { dialog, which ->
                track.id?.let {
                    viewModel.deleteTrack(
                        it,
                        requireArguments().parcelable<PlayList>(PLAYLIST_ITEM)?.id ?: 0
                    )
                }
            }

        confirmDialog.show()
    }

    private fun deletePlaylist(id: Long, name: String) {
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setMessage("Хотите удалить плейлист «$name»?")
            .setNegativeButton("Нет") { dialog, which -> }
            .setPositiveButton("Да") { dialog, which ->
                viewModel.deletePlaylist(id)
                findNavController().popBackStack()
            }

        confirmDialog.show()
    }

    override fun onResume() {
        super.onResume()
        debounceClick = true
        BottomSheetBehavior.from(binding.bottomSheetMore).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        viewModel.updatePlayList(requireArguments().parcelable<PlayList>(PLAYLIST_ITEM)!!.id)
    }

    companion object {
        const val PLAYLIST_ITEM = "Playlist"
        const val TRACK_KEY = "TRACK"
    }
}
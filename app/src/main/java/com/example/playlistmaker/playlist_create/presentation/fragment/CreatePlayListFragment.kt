package com.example.playlistmaker.playlist_create.presentation.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.app.showCustomToast
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.playlist_create.presentation.view_model.CreatePlayListFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlayListFragment : Fragment() {
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreatePlayListFragmentViewModel by viewModel()
    lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickVisualMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    imageUri = uri
                    binding.imageInsert.setImageURI(uri)
                }
            }
        binding.imageInsert.setOnClickListener {
            pickVisualMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.bCreate.isEnabled = false
        binding.etNamePlayList.isActivated = false
        binding.etAboutPlayList.isActivated =false

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which -> }
            .setPositiveButton("Завершить") { dialog, which -> findNavController().popBackStack() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exitToView()
            }
        })

        binding.etNamePlayList.doOnTextChanged { text, start, before, count ->
            if (binding.etNamePlayList.text.isNotBlank()) {
                binding.bCreate.isEnabled = true
            } else {
                binding.bCreate.isEnabled = false
            }

            if (binding.etNamePlayList.text.isNotEmpty()) {
                binding.etNamePlayList.isActivated = true
            } else {
                binding.etNamePlayList.isActivated = false
            }
        }

        binding.etAboutPlayList.doOnTextChanged { text, start, before, count ->
            if (binding.etAboutPlayList.text.isNotEmpty()) {
                binding.etAboutPlayList.isActivated = true
            } else {
                binding.etAboutPlayList.isActivated = false
            }
        }

        binding.bCreate.setOnClickListener {
            Toast(requireContext()).showCustomToast(
                getString(
                    R.string.playlist_create,
                    binding.etNamePlayList.text
                ), requireActivity())
            viewModel.createNewPlayList(
                binding.etNamePlayList.text.toString(),
                binding.etAboutPlayList.text.toString(),
                imageUri
            )
            findNavController().popBackStack()
        }

        viewModel.nameImage

        binding.backIv.setOnClickListener {
            exitToView()
        }
    }

    private fun exitToView() {
        if (!binding.etAboutPlayList.text.isNullOrEmpty() || !binding.etNamePlayList.text.isNullOrEmpty() || binding.imageInsert.drawable != null) {
            confirmDialog.show()
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
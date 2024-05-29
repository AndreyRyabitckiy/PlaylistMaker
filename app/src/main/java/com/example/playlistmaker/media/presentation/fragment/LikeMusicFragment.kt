package com.example.playlistmaker.media.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.playlistmaker.databinding.FragmentLikeMusicBinding
import com.example.playlistmaker.media.presentation.view_model.LikeMusicViewModel

class LikeMusicFragment: Fragment() {

    private val viewModel: LikeMusicViewModel by viewModels<LikeMusicViewModel>()
    private var _binding: FragmentLikeMusicBinding? = null
    private val binding get() = _binding!!

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LIKE_MUSIC_NUMBER = "like_music_number"

        fun newInstance() = LikeMusicFragment().apply {
            arguments = Bundle().apply {
                putInt(LIKE_MUSIC_NUMBER, 1)
            }
        }
    }
}
package com.example.playlistmaker.webview.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentWebViewBinding
import com.example.playlistmaker.ui.root.RootActivity

class WebViewFragment : Fragment() {
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            arguments?.getString(URL_KEY)?.let { url ->
                webview.loadUrl(url)
            }
        }
        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val URL_KEY = "URL_KEY"
    }
}
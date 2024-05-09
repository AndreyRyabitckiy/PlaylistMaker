package com.example.playlistmaker.search.presentation.fragment

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.models.ResponseStatus
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.view_model.SearchFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment:Fragment() {
    private var binding: FragmentSearchBinding? = null
    private val viewModel: SearchFragmentViewModel by viewModel<SearchFragmentViewModel>()
    private val searchRunnable = Runnable { sendToServer() }
    private var searchString = ""
    private var showHistory = false
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    val adapter by lazy { TrackAdapter() }
    private var searchStatus = ResponseStatus.SUCCESS
    val historyTracks: ArrayList<Track> = arrayListOf()
    val tracks = ArrayList<Track>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sharedPrefsWork(USE_READ)
        viewModel.tracksHistory.observe(viewLifecycleOwner) {
            historyTracks.clear()
            historyTracks.addAll(it)
        }

        viewModel.tracks.observe(viewLifecycleOwner) {
            tracks.clear()
            binding?.run {
                when (searchStatus) {
                    ResponseStatus.SUCCESS -> {
                        tracks.addAll(it)
                        if (tracks.isNotEmpty()) {
                            progressBar.isVisible = false
                            rwTrack.isVisible = true
                            showHistory = false
                            adapter.data = tracks
                        }
                        if (tracks.isEmpty()) {
                            progressBar.isVisible = false
                            rwTrack.isVisible = false
                            llHolderNothingOrWrong.isVisible = true
                            ivSunOrWiFi.setImageResource(R.drawable.sun_ic)
                            tvTextHolder.setText(R.string.nothing)
                            btReserch.isVisible = false
                        }
                    }

                    ResponseStatus.ERROR -> {
                        progressBar.isVisible = false
                        rwTrack.isVisible = false
                        llHolderNothingOrWrong.isVisible = true
                        ivSunOrWiFi.setImageResource(R.drawable.nointernet_ic)
                        tvTextHolder.setText(R.string.Wrong)
                        btReserch.isVisible = true
                    }
                }
            }
        }

        viewModel.searchStatus.observe(viewLifecycleOwner) {
            searchStatus = it
        }

        fun clickDebounce(): Boolean  {
            val current = isClickAllowed
            if (isClickAllowed) {
                isClickAllowed = false
                handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
            }
            return current
        }

        adapter.onClick = { item ->
            if (clickDebounce()) {
                viewModel.sharedPrefsWork(USE_WRITE, item)
                findNavController().navigate(
                    R.id.action_searchFragment_to_musicPlayerFragment,
                    bundleOf(TRACK_KEY to item)
                )
            }
        }
        binding?.run {
            rwTrack.adapter = adapter

            etSearchText.setText(searchString)

            bClearHistorySearch.setOnClickListener {
                adapter.data.clear()
                viewModel.sharedPrefsWork(USE_CLEAR)
                tvHistorySearch.isVisible = false
                bClearHistorySearch.isVisible = false
            }

            etSearchText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && etSearchText.text.isEmpty()) {
                    showHistory = true
                    adapter.data = historyTracks
                    if (historyTracks.isNotEmpty()) {
                        tvHistorySearch.isVisible = true
                        bClearHistorySearch.isVisible = true
                    }
                } else {
                    tvHistorySearch.isVisible = false
                    bClearHistorySearch.isVisible = false
                }

            }

            val simpleTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    showHistory = true
                    adapter.data = historyTracks
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    searchDebounce()
                    ivClearIcon.isVisible = !s.isNullOrEmpty()
                    searchString = etSearchText.toString()

                    if (etSearchText.hasFocus() && s?.isEmpty() == true) {
                        if (historyTracks.isNotEmpty()) {
                            rwTrack.isVisible = true
                            tvHistorySearch.isVisible = true
                            bClearHistorySearch.isVisible = true
                        }
                        llHolderNothingOrWrong.isVisible = false
                    } else {
                        showHistory = false
                        adapter.data = tracks
                        tvHistorySearch.isVisible = false
                        bClearHistorySearch.isVisible = false
                        llHolderNothingOrWrong.isVisible = false
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    // empty
                }
            }

            etSearchText.addTextChangedListener(simpleTextWatcher)

            ivClearIcon.setOnClickListener {
                etSearchText.setText("")
                val inputMethodManager =
                    requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(etSearchText.windowToken, 0)
                tracks.clear()
                showHistory = true
                adapter.data = historyTracks
            }

            btReserch.setOnClickListener {
                sendToServer()
            }

            etSearchText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendToServer()
                }
                false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (showHistory) {
            adapter.data = historyTracks
        }
    }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun sendToServer() = binding?.run {
        if (etSearchText.text.isNotBlank()) {
            viewModel.searchTracks(etSearchText.text.toString())
            rwTrack.isVisible = false
            llHolderNothingOrWrong.isVisible = false
            btReserch.isVisible = false
            progressBar.isVisible = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        private const val TRACK_KEY = "TRACK"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val USE_CLEAR = "clear"
        private const val USE_READ = "read"
        private const val USE_WRITE = "write"
    }
}
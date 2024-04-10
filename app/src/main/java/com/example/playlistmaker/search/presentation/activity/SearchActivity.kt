package com.example.playlistmaker.search.presentation.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.models.ResponseStatus
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.presentation.activity.MusicPlayerActivity
import com.example.playlistmaker.search.presentation.view_model.SearchActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private val viewModel:SearchActivityViewModel by viewModel<SearchActivityViewModel>()

    private val searchRunnable = Runnable { sendToServer() }
    private var searchString = ""
    private lateinit var binding: ActivitySearchBinding
    private var showHistory = false
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    val adapter by lazy { TrackAdapter() }
    private var searchStatus = ResponseStatus.SUCCESS


    val historyTracks: ArrayList<Track> = arrayListOf()

    val tracks = ArrayList<Track>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH, searchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchString = savedInstanceState.getString(SEARCH).toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.sharedPrefsWork(USE_READ)
        viewModel.tracksHistory.observe(this) {
            historyTracks.clear()
            historyTracks.addAll(it)
        }

        viewModel.tracks.observe(this) {
            tracks.clear()
            when (searchStatus) {
                ResponseStatus.SUCCESS -> {
                    tracks.addAll(it)
                    if (tracks.isNotEmpty()) {
                        binding.progressBar.isVisible = false
                        binding.rwTrack.isVisible = true
                        showHistory = false
                        adapter.data = tracks
                    }
                    if (tracks.isEmpty()) {
                        binding.progressBar.isVisible = false
                        binding.rwTrack.isVisible = false
                        binding.llHolderNothingOrWrong.isVisible = true
                        binding.ivSunOrWiFi.setImageResource(R.drawable.sun_ic)
                        binding.tvTextHolder.setText(R.string.nothing)
                        binding.btReserch.isVisible = false
                    }
                }

                ResponseStatus.ERROR -> {
                    binding.progressBar.isVisible = false
                    binding.rwTrack.isVisible = false
                    binding.llHolderNothingOrWrong.isVisible = true
                    binding.ivSunOrWiFi.setImageResource(R.drawable.nointernet_ic)
                    binding.tvTextHolder.setText(R.string.Wrong)
                    binding.btReserch.isVisible = true
                }
            }
        }

        viewModel.searchStatus.observe(this) {
            searchStatus = it
        }

        fun clickDebounce(): Boolean {
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
                val playerIntent = Intent(this@SearchActivity, MusicPlayerActivity::class.java)
                playerIntent.putExtra(MusicPlayerActivity.TRACK_KEY, item)
                startActivity(playerIntent)
            }
        }

        binding.rwTrack.adapter = adapter

        binding.etSearchText.setText(searchString)

        binding.ivToolBar.setOnClickListener {
            finish()
        }

        binding.bClearHistorySearch.setOnClickListener {
            adapter.data.clear()
            viewModel.sharedPrefsWork(USE_CLEAR)
            binding.tvHistorySearch.isVisible = false
            binding.bClearHistorySearch.isVisible = false
        }

        binding.etSearchText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.etSearchText.text.isEmpty()) {
                showHistory = true
                adapter.data = historyTracks
                if (historyTracks.isNotEmpty()) {
                    binding.tvHistorySearch.isVisible = true
                    binding.bClearHistorySearch.isVisible = true
                }
            } else {
                binding.tvHistorySearch.isVisible = false
                binding.bClearHistorySearch.isVisible = false
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                showHistory = true
                adapter.data = historyTracks
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                binding.ivClearIcon.isVisible = !s.isNullOrEmpty()
                searchString = binding.etSearchText.toString()

                if (binding.etSearchText.hasFocus() && s?.isEmpty() == true) {
                    if (historyTracks.isNotEmpty()) {
                        binding.rwTrack.isVisible = true
                        binding.tvHistorySearch.isVisible = true
                        binding.bClearHistorySearch.isVisible = true
                    }
                    binding.llHolderNothingOrWrong.isVisible = false
                } else {
                    showHistory = false
                    adapter.data = tracks
                    binding.tvHistorySearch.isVisible = false
                    binding.bClearHistorySearch.isVisible = false
                    binding.llHolderNothingOrWrong.isVisible = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        binding.etSearchText.addTextChangedListener(simpleTextWatcher)

        binding.ivClearIcon.setOnClickListener {
            binding.etSearchText.setText("")
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.etSearchText.windowToken, 0)
            tracks.clear()
            showHistory = true
            adapter.data = historyTracks
        }

        binding.btReserch.setOnClickListener {
            sendToServer()
        }

        binding.etSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendToServer()
            }
            false
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun sendToServer() {
        if (binding.etSearchText.text.isNotBlank()) {
            viewModel.searchTracks(binding.etSearchText.text.toString())
            binding.rwTrack.isVisible = false
            binding.llHolderNothingOrWrong.isVisible = false
            binding.btReserch.isVisible = false
            binding.progressBar.isVisible = true
        }
    }


    override fun onResume() {
        super.onResume()
        if (showHistory) {
            adapter.data = historyTracks
        }
    }

    companion object {
        private const val SEARCH = "SEARCH"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val USE_CLEAR = "clear"
        private const val USE_READ = "read"
        private const val USE_WRITE = "write"

    }
}
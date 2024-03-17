package com.example.playlistmaker.presentation.search

import android.annotation.SuppressLint
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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.ResponseStatus
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.sharedprefs.SharedPrefsInteractor
import com.example.playlistmaker.presentation.player.MusicPlayerActivity

class SearchActivity : AppCompatActivity() {

    private val searchRunnable = Runnable { sendToServer() }

    private var searchString = ""

    private val searchField by lazy { findViewById<EditText>(R.id.etSearchText) }
    private val rwTrack by lazy { findViewById<RecyclerView>(R.id.rwTrack) }
    private val holderNothingOrWrong by lazy { findViewById<LinearLayout>(R.id.llHolderNothingOrWrong) }
    private val buttonResearch by lazy { findViewById<Button>(R.id.btReserch) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progressBar) }
    private val sunOrWiFi by lazy { findViewById<ImageView>(R.id.ivSunOrWiFi) }
    private val textHolder by lazy { findViewById<TextView>(R.id.tvTextHolder) }

    private val sharedPrefs = Creator.SharedPrefsInteractor()

    private var showHistory = false

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    val adapter by lazy { TrackAdapter() }

    val searchHistory by lazy { getSharedPreferences(HISTORY_MAIN, MODE_PRIVATE) }


    val historyTracks: ArrayList<Track>
        get() {
            val answer = ArrayList<Track>()
            sharedPrefs.readWriteClear(
                searchHistory,
                USE_READ,
                null,
                consumer = object : SharedPrefsInteractor.SharedPrefsConsumer {
                    override fun consume(foundSharedPrefs: ArrayList<Track>) {
                        answer.addAll(foundSharedPrefs)
                    }
                })
            return answer
        }

    val tracks = ArrayList<Track>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH, searchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchString = savedInstanceState.getString(SEARCH).toString()
    }

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

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
                sharedPrefs.readWriteClear(
                    searchHistory,
                    USE_WRITE,
                    item,
                    consumer = object : SharedPrefsInteractor.SharedPrefsConsumer {
                        override fun consume(foundSharedPrefs: ArrayList<Track>) {
                            Unit
                        }
                    })
                val playerIntent = Intent(this@SearchActivity, MusicPlayerActivity::class.java)
                playerIntent.putExtra(MusicPlayerActivity.TRACK_KEY, item)
                startActivity(playerIntent)
            }
        }

        rwTrack.adapter = adapter
        val backButton = findViewById<ImageView>(R.id.ivToolBar)
        val clearButton = findViewById<ImageView>(R.id.ivClearIcon)
        val textHistorySearch = findViewById<TextView>(R.id.tvHistorySearch)
        val buttonClearHistorySearch = findViewById<Button>(R.id.bClearHistorySearch)

        searchField.setText(searchString)

        backButton.setOnClickListener {
            finish()
        }

        buttonClearHistorySearch.setOnClickListener {
            adapter.data.clear()
            sharedPrefs.readWriteClear(
                searchHistory,
                USE_CLEAR,
                null,
                consumer = object : SharedPrefsInteractor.SharedPrefsConsumer {
                    override fun consume(foundSharedPrefs: ArrayList<Track>) {
                        Unit
                    }
                })
            textHistorySearch.isVisible = false
            buttonClearHistorySearch.isVisible = false
        }

        searchField.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchField.text.isEmpty()) {
                showHistory = true
                adapter.data = historyTracks
                if (historyTracks.isNotEmpty()) {
                    textHistorySearch.isVisible = true
                    buttonClearHistorySearch.isVisible = true
                }
            } else {
                textHistorySearch.isVisible = false
                buttonClearHistorySearch.isVisible = false
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                showHistory = true
                adapter.data = historyTracks
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                clearButton.isVisible = !s.isNullOrEmpty()
                searchString = searchField.toString()

                if (searchField.hasFocus() && s?.isEmpty() == true) {
                    if (historyTracks.isNotEmpty()) {
                        rwTrack.isVisible = true
                        textHistorySearch.isVisible = true
                        buttonClearHistorySearch.isVisible = true
                    }
                    holderNothingOrWrong.isVisible = false
                } else {
                    showHistory = false
                    adapter.data = tracks
                    textHistorySearch.isVisible = false
                    buttonClearHistorySearch.isVisible = false
                    holderNothingOrWrong.isVisible = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        searchField.addTextChangedListener(simpleTextWatcher)

        clearButton.setOnClickListener {
            searchField.setText("")
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchField.windowToken, 0)
            tracks.clear()
            showHistory = true
            adapter.data = historyTracks
        }

        buttonResearch.setOnClickListener {
            sendToServer()
        }

        searchField.setOnEditorActionListener { view, actionId, event ->
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
        if (searchField.text.isNotEmpty()) {
            rwTrack.isVisible = false
            holderNothingOrWrong.isVisible = false
            buttonResearch.isVisible = false
            progressBar.isVisible = true
            val interactor = Creator.provideTracksInteractor()
            interactor.searchTracks(
                expression = searchField.text.toString(),
                consumer = object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTrack: List<Track>, status: ResponseStatus) {
                        runOnUiThread {
                            rwTrack.isVisible = true
                            holderNothingOrWrong.isVisible = false

                            progressBar.isVisible = false
                            tracks.clear()

                            when (status) {
                                ResponseStatus.SUCCESS -> {
                                    if (foundTrack.isNotEmpty()) {
                                        tracks.addAll(foundTrack)
                                        showHistory = false
                                        adapter.data = tracks
                                    }
                                    if (tracks.isEmpty()) {
                                        progressBar.isVisible = false
                                        rwTrack.isVisible = false
                                        holderNothingOrWrong.isVisible = true
                                        sunOrWiFi.setImageResource(R.drawable.sun_ic)
                                        textHolder.setText(R.string.nothing)
                                        buttonResearch.isVisible = false
                                    }
                                }

                                ResponseStatus.ERROR -> {
                                    progressBar.isVisible = false
                                    rwTrack.isVisible = false
                                    holderNothingOrWrong.isVisible = true
                                    sunOrWiFi.setImageResource(R.drawable.nointernet_ic)
                                    textHolder.setText(R.string.Wrong)
                                    buttonResearch.isVisible = true
                                }
                            }
                        }

                    }
                })
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
        private const val HISTORY_MAIN = "historyMain"
        private const val USE_CLEAR = "clear"
        private const val USE_READ = "read"
        private const val USE_WRITE = "write"

    }
}
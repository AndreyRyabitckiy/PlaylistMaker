package com.example.playlistmaker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesApi::class.java)

    private var searchString = ""

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

        val sharedPrefs = getSharedPreferences(SearchHistory.HISTORY_MAIN, MODE_PRIVATE)
        var historyTracks = SearchHistory(sharedPrefs).read()
        val tracks = ArrayList<Track>()

        val onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(item: Track) {
                historyTracks = SearchHistory(sharedPrefs).write(historyTracks, item)
            }
        }

        val trackAdapter = TrackAdapter(tracks, onItemClickListener)
        val historyAdapter = TrackAdapter(historyTracks)

        val rwTrack = findViewById<RecyclerView>(R.id.rwTrack)
        val backButton = findViewById<ImageView>(R.id.ivToolBar)
        val clearButton = findViewById<ImageView>(R.id.ivClearIcon)
        val searchField = findViewById<EditText>(R.id.etSearchText)
        val holderNothingOrWrong = findViewById<LinearLayout>(R.id.llHolderNothingOrWrong)
        val sunOrWiFi = findViewById<ImageView>(R.id.ivSunOrWiFi)
        val textHolder = findViewById<TextView>(R.id.tvTextHolder)
        val buttonResearch = findViewById<Button>(R.id.btReserch)
        val textHistorySearch = findViewById<TextView>(R.id.tvHistorySearch)
        val buttonClearHistorySearch = findViewById<Button>(R.id.bClearHistorySearch)

        searchField.setText(searchString)

        backButton.setOnClickListener {
            finish()
        }

        buttonClearHistorySearch.setOnClickListener {
            historyTracks.clear()
            SearchHistory(sharedPrefs).write(historyTracks)
            historyAdapter.notifyDataSetChanged()
            textHistorySearch.isVisible = false
            buttonClearHistorySearch.isVisible = false
        }

        searchField.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchField.text.isEmpty()) {
                rwTrack.adapter = historyAdapter
                historyAdapter.notifyDataSetChanged()
                if(historyTracks.isNotEmpty()) {
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
                rwTrack.adapter = historyAdapter
                historyAdapter.notifyDataSetChanged()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                searchString = searchField.toString()

                if (searchField.hasFocus() && s?.isEmpty() == true) {
                    if(historyTracks.isNotEmpty()) {
                        textHistorySearch.isVisible = true
                        buttonClearHistorySearch.isVisible = true
                    }
                    holderNothingOrWrong.isVisible = false
                } else {
                    rwTrack.adapter = trackAdapter
                    trackAdapter.notifyDataSetChanged()
                    textHistorySearch.isVisible = false
                    buttonClearHistorySearch.isVisible = false
                    holderNothingOrWrong.isVisible = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }

        }

        fun sendToServer() {
            if (searchField.text.isNotEmpty()) {
                iTunesService.search(searchField.text.toString())
                    .enqueue(object : Callback<TrackResponse> {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(
                            call: Call<TrackResponse>,
                            response: Response<TrackResponse>
                        ) {
                            rwTrack.isVisible = true
                            holderNothingOrWrong.isVisible = false

                            if (response.isSuccessful) {
                                tracks.clear()
                                val answer = response.body()?.results
                                if (answer?.isNotEmpty() == true) {
                                    tracks.addAll(response.body()?.results!!)
                                    trackAdapter.notifyDataSetChanged()
                                }
                                if (tracks.isEmpty()) {
                                    rwTrack.isVisible = false
                                    holderNothingOrWrong.isVisible = true
                                    sunOrWiFi.setImageResource(R.drawable.sun_ic)
                                    textHolder.setText(R.string.nothing)
                                    buttonResearch.isVisible = false
                                }
                            } else {
                                rwTrack.isVisible = false
                                holderNothingOrWrong.isVisible = true
                                sunOrWiFi.setImageResource(R.drawable.nointernet_ic)
                                textHolder.setText(R.string.Wrong)
                                buttonResearch.isVisible = true
                            }
                        }

                        override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                            rwTrack.isVisible = false
                            holderNothingOrWrong.isVisible = true
                            sunOrWiFi.setImageResource(R.drawable.nointernet_ic)
                            textHolder.setText(R.string.Wrong)
                            buttonResearch.isVisible = true
                        }
                    })
            }
        }

        searchField.addTextChangedListener(simpleTextWatcher)

        clearButton.setOnClickListener {
            searchField.setText("")
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchField.windowToken, 0)
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
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


    companion object {
        const val SEARCH = "SEARCH"
        const val AMOUNT = ""
    }
}
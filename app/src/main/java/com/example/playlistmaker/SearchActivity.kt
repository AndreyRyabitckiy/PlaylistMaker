package com.example.playlistmaker

import android.annotation.SuppressLint
import android.app.Activity
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.Locale

class SearchActivity : AppCompatActivity() {
    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesApi::class.java)

    private var searchString = ""

    private val tracks = ArrayList<Track>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH,searchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchString = savedInstanceState.getString(SEARCH).toString()
    }
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val trackAdapter = TrackAdapter(tracks)

        val rwTrack = findViewById<RecyclerView>(R.id.rwTrack)
        val backButton = findViewById<ImageView>(R.id.ivToolBar)
        val clearButton = findViewById<ImageView>(R.id.ivClearIcon)
        val searchField = findViewById<EditText>(R.id.etSearchText)
        val holderNothing = findViewById<FrameLayout>(R.id.holderNothing)
        val holderWrong = findViewById<FrameLayout>(R.id. holdeWrong)
        val researchButton = findViewById<Button>(R.id.research)

        rwTrack.adapter = trackAdapter

        searchField.setText(searchString)

        backButton.setOnClickListener {
            finish()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()

                searchString = searchField.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }

        }
        searchField.addTextChangedListener(simpleTextWatcher)

        clearButton.setOnClickListener {
            searchField.setText("")
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchField.windowToken, 0)
            tracks.clear()
            trackAdapter.notifyDataSetChanged()

        }

        researchButton.setOnClickListener {

        }

        searchField.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                if(searchField.text.isNotEmpty()){
                    iTunesService.search(searchField.text.toString()).enqueue(object : Callback<TrackResponse> {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(
                            call: Call<TrackResponse>,
                            response: Response<TrackResponse>) {
                            holderWrong.isVisible = false
                            rwTrack.isVisible = true
                            holderNothing.isVisible = false
                            if (response.code() == 200){
                                tracks.clear()
                                if (response.body()?.results?.isNotEmpty() == true){
                                    tracks.addAll(response.body()?.results!!)
                                    trackAdapter.notifyDataSetChanged()
                                }
                                if (tracks.isEmpty()){

                                    rwTrack.isVisible = false
                                    holderNothing.isVisible = true
                                }
                            } else {
                                rwTrack.isVisible = false
                                holderWrong.isVisible = true

                            }
                        }

                        override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                            rwTrack.isVisible = false
                            holderWrong.isVisible = true
                        }
                    } )
                }
            }
            false
        }
    }

    companion object{
        const val SEARCH = "SEARCH"
        const val AMOUNT = ""
    }
}



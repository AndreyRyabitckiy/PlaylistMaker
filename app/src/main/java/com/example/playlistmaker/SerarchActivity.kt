package com.example.playlistmaker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView

class SerarchActivity : AppCompatActivity() {
    var searchString = ""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH",searchString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchString = savedInstanceState.getString("SEARCH").toString()
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serarch)

        val backButton = findViewById<ImageView>(R.id.toolBar)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val searchField = findViewById<EditText>(R.id.searchText)

        searchField.setText(searchString)

        backButton.setOnClickListener {
            finish()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
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
        }
    }

    private fun clearButtonVisibility(s:CharSequence?):Int {
        return if (s.isNullOrEmpty()) {
            View.GONE

        } else {
            View.VISIBLE
        }
    }
}
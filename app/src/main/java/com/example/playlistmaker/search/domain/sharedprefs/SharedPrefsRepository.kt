package com.example.playlistmaker.search.domain.sharedprefs

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.models.Track

interface SharedPrefsRepository {
    fun saveReadClear(
        sharedPreferences: SharedPreferences,
        use: String,
        track: Track?
    ): ArrayList<Track>
}
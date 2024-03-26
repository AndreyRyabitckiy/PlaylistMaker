package com.example.playlistmaker.domain.sharedprefs

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track

interface SharedPrefsRepository {
    fun saveReadClear(
        sharedPreferences: SharedPreferences,
        use: String,
        track: Track?
    ): ArrayList<Track>
}
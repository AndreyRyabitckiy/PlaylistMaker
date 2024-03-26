package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.models.Track

interface SearchHistoryStorage {
    fun read(sharedPreferences: SharedPreferences): ArrayList<TrackDto>

    fun write(sharedPreferences: SharedPreferences, track: Track?): ArrayList<TrackDto>

    fun clearHistory(sharedPreferences: SharedPreferences): ArrayList<TrackDto>
}
package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val MAX_HISTORY_SIZE = 10

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun read(): ArrayList<Track> {
        val json = sharedPreferences.getString(HISTORY, null) ?: return ArrayList<Track>()
        return Gson().fromJson(json, Array<Track>::class.java).toCollection(ArrayList())

    }

    fun write(addTrack: Track): ArrayList<Track> {
        var trackList = read()

        trackList.find { it.trackId == addTrack.trackId }
            ?.let {
                trackList.remove(it)
            }

        trackList.add(0, addTrack)
        if (trackList.size > MAX_HISTORY_SIZE) {
            trackList = ArrayList(trackList.subList(0, MAX_HISTORY_SIZE - 1))
        }
        val json = Gson().toJson(trackList)
        sharedPreferences.edit().putString(HISTORY, json).apply()
        return trackList
    }

    fun clearHistory() {
        sharedPreferences.edit().remove(HISTORY).apply()
    }

    companion object {
        private const val HISTORY = "history"
        const val HISTORY_MAIN = "historyMain"
    }
}
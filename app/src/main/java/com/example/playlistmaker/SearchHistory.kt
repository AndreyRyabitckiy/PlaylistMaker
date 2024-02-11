package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun read(): ArrayList<Track> {
        val json = sharedPreferences.getString(HISTORY, null) ?: return ArrayList<Track>()
        return Gson().fromJson(json, Array<Track>::class.java).toCollection(ArrayList())

    }

    fun write(trackList: ArrayList<Track>, addTrack: Track? = null): ArrayList<Track> {
        trackList.reverse()
        if (addTrack == null) {
            val json = Gson().toJson(trackList)
            sharedPreferences.edit().putString(HISTORY, json).apply()

        } else {
            val trackId = addTrack.trackId
            val iterator = trackList.iterator()
            for (items in iterator) {
                if (items.trackId == trackId) {
                    iterator.remove()
                }
            }

            trackList.add(addTrack!!)
            if (trackList.size >= 11) {
                trackList.removeAt(0)
            }
            val json = Gson().toJson(trackList)
            sharedPreferences.edit().putString(HISTORY, json).apply()
        }
        trackList.reverse()
        return trackList
    }

    companion object {
        const val HISTORY = "history"
        const val HISTORY_MAIN = "historyMain"
    }
}
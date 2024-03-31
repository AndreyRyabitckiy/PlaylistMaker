package com.example.playlistmaker.search.data.sharedstorage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.search.data.SearchHistoryStorage
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

private const val MAX_HISTORY_SIZE = 10

class SearchHistoryStorageImpl(private val context: Context) : SearchHistoryStorage {

    override fun read(): ArrayList<TrackDto> {
        val sharedPreferences = context.getSharedPreferences(HISTORY_MAIN, MODE_PRIVATE)
        val json = sharedPreferences.getString(HISTORY, null) ?: return ArrayList()
        return Gson().fromJson(json, Array<TrackDto>::class.java).toCollection(ArrayList())

    }

    override fun write(addTrack: Track?): ArrayList<TrackDto> {
        val sharedPreferences = context.getSharedPreferences(HISTORY_MAIN, MODE_PRIVATE)
        var trackList = read()
        val addTrackDto = TrackDto(addTrack?.trackName.toString(), addTrack?.artistName.toString(), addTrack?.trackTimeMillis.toString(), addTrack?.artworkUrl100.toString(), addTrack?.trackId.toString(), addTrack?.collectionName.toString(), addTrack?.releaseDate.toString(), addTrack?.primaryGenreName.toString(), addTrack?.country.toString(), addTrack?.previewUrl.toString())

        trackList.find { it.trackId == addTrack!!.trackId }
            ?.let {
                trackList.remove(it)
            }

        trackList.add(0, addTrackDto)
        if (trackList.size > MAX_HISTORY_SIZE) {
            trackList = ArrayList(trackList.subList(0, MAX_HISTORY_SIZE - 1))
        }
        val json = Gson().toJson(trackList)
        sharedPreferences.edit().putString(HISTORY, json).apply()
        return trackList
    }

    override fun clearHistory(): ArrayList<TrackDto> {
        val sharedPreferences = context.getSharedPreferences(HISTORY_MAIN, MODE_PRIVATE)
        sharedPreferences.edit().remove(HISTORY).apply()
        return read()

    }

    companion object {
        private const val HISTORY = "history"
        private const val HISTORY_MAIN = "historyMain"
    }
}
package com.example.playlistmaker.search.data.sharedstorage

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.SearchHistoryStorage
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

private const val MAX_HISTORY_SIZE = 10

class SearchHistoryStorageImpl : SearchHistoryStorage {

    override fun read(sharedPreferences: SharedPreferences): ArrayList<TrackDto> {
        val json = sharedPreferences.getString(HISTORY, null) ?: return ArrayList()
        return Gson().fromJson(json, Array<TrackDto>::class.java).toCollection(ArrayList())

    }

    override fun write(sharedPreferences: SharedPreferences,addTrack: Track?): ArrayList<TrackDto> {
        var trackList = read(sharedPreferences)
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

    override fun clearHistory(sharedPreferences: SharedPreferences): ArrayList<TrackDto> {
        sharedPreferences.edit().remove(HISTORY).apply()
        return read(sharedPreferences)

    }

    companion object {
        private const val HISTORY = "history"
    }
}
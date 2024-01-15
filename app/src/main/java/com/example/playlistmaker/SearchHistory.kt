package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.view.View
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {



    fun read (): ArrayList<Track>{
        val json = sharedPreferences.getString(HISTORY,null)?:return ArrayList()
        return Gson().fromJson(json,ArrayList<Track>()::class.java)
    }

    fun write (track: ArrayList<Track>){
        val json = Gson().toJson(track)
        sharedPreferences.edit()
            .putString(HISTORY, json)
            .apply()
    }

    companion object {
        const val HISTORY = "history"
    }
}
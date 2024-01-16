package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPreferences: SharedPreferences) {



    fun read (): ArrayList<Track>{
        val json = sharedPreferences.getString(HISTORY,null)?:return ArrayList<Track>()
        return Gson().fromJson(json,ArrayList<Track>()::class.java)

    }

    fun write (track: ArrayList<Track>){
        val json = Gson().toJson(track)
        sharedPreferences.edit().putString(HISTORY, json).apply()
    }

    companion object {
        const val HISTORY = "history"
        const val HISTORY_MAIN = "historyMain"
    }
}
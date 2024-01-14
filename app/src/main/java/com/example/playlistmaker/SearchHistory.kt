package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.view.View
import com.google.gson.Gson

class SearchHistory( sharedPreferences: SharedPreferences) {



    fun read (sharedPreferences: SharedPreferences): Array<Track>{
        val json = sharedPreferences.getString(HISTORY,null)?:return emptyArray()
        return Gson().fromJson(json,Array<Track>::class.java)


    }

    fun write (sharedPreferences: SharedPreferences,track: Array<Track>){
        val json = Gson().toJson(track)
        sharedPreferences.edit()
            .putString(HISTORY, json)
            .apply()

    }

    companion object {
        const val HISTORY = "history"
    }
}
package com.example.playlistmaker.playlist_create.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayList (
    val id: Long = 0,
    val namePlayList: String,
    val aboutPlayList: String?,
    val roadToFileImage: String,
    val count: Int = 0,
) : Parcelable
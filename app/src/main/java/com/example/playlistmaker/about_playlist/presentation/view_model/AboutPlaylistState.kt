package com.example.playlistmaker.about_playlist.presentation.view_model

import com.example.playlistmaker.search.domain.models.Track

data class AboutPlaylistState(
    val tracks: List<Track>,
    val time: String,
    val count: String,
)
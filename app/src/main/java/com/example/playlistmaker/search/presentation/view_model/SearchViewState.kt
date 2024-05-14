package com.example.playlistmaker.search.presentation.view_model

import com.example.playlistmaker.search.domain.models.ResponseStatus
import com.example.playlistmaker.search.domain.models.Track

data class SearchViewState(
    val searchText: String = "",
    val searchStatus: ResponseStatus = ResponseStatus.SUCCESS,
    val tracks: List<Track> = emptyList(),
    val showHistory: Boolean = false,
    val isClickAllowed: Boolean = true
)
package com.example.playlistmaker.domain.models

data class TrackResults(
    val status: ResponseStatus,
    val data: List<Track> = emptyList()
)


enum class ResponseStatus {
    SUCCESS,
    ERROR
}
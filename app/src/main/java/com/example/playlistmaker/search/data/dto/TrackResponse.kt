package com.example.playlistmaker.search.data.dto

class TrackResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()
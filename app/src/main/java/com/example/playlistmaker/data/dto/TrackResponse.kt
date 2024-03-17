package com.example.playlistmaker.data.dto

class TrackResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()
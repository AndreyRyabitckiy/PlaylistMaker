package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.ResponseStatus
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TrackResults
import java.text.SimpleDateFormat
import java.util.Locale

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): TrackResults {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return if (response.resultCode == 200) {
            TrackResults(
                status = ResponseStatus.SUCCESS,
                data = (response as TrackResponse).results.map {
                    Track(
                        trackName = it.trackName,
                        artistName = it.artistName,
                        trackTimeMillis = getFormattedTime(it.trackTimeMillis),
                        artworkUrl100 = it.artworkUrl100,
                        trackId = it.trackId,
                        collectionName = it.collectionName,
                        releaseDate = getFormattedDate(it.releaseDate),
                        primaryGenreName = it.primaryGenreName,
                        country = it.country,
                        previewUrl = it.previewUrl
                    )
                }
            )
        } else {
            TrackResults(
                status = ResponseStatus.ERROR
            )
        }
    }

    private fun getFormattedDate(inputDate: String?): String {
        inputDate ?: return ""

        val dfIn = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val dfOut = SimpleDateFormat("yyyy", Locale.getDefault())

        dfIn.parse(inputDate)?.let { date ->
            return dfOut.format(date).orEmpty() ?: ""
        } ?: return ""
    }

    private fun getFormattedTime(inputDate: String?): String {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        val dfOut = dateFormat.format(inputDate?.toLong())
        return dfOut
    }
}
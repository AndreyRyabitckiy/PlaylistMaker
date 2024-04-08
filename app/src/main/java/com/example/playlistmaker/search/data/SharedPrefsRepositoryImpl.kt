package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.sharedprefs.SharedPrefsRepository

class SharedPrefsRepositoryImpl(private val storage: SearchHistoryStorage) : SharedPrefsRepository {
    override fun saveReadClear(
        use: String,
        track: Track?
    ): ArrayList<Track> {
        val answer = ArrayList<Track>()

        when (use) {
            USE_READ -> answer.addAll(trackDtoToTrack(storage.read()))
            USE_WRITE -> answer.addAll(trackDtoToTrack(storage.write(track)))
            USE_CLEAR -> {
                storage.clearHistory()
                answer.addAll(trackDtoToTrack(storage.read()))
            }
        }
        return answer
    }

    private fun trackDtoToTrack(list:ArrayList<TrackDto>):ArrayList<Track>{
        val answer = list.map {
            Track(
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl100 = it.artworkUrl100,
                trackId = it.trackId,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                previewUrl = it.previewUrl
            )
        }
        return answer.toCollection(ArrayList())
    }

    companion object{
        private const val USE_CLEAR = "clear"
        private const val USE_READ = "read"
        private const val USE_WRITE = "write"
    }
}
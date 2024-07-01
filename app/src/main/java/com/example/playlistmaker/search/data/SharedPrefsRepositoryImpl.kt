package com.example.playlistmaker.search.data

import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.sharedprefs.SharedPrefsRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SharedPrefsRepositoryImpl(
    private val storage: SearchHistoryStorage,
    private val likeTrackDatabase: AppDatabase
) : SharedPrefsRepository {
    override suspend fun saveReadClear(
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

    private fun trackDtoToTrack(list: ArrayList<TrackDto>): ArrayList<Track> {
        val listTrackId = ArrayList<String>()
        MainScope().launch {
            listTrackId.addAll(likeTrackDatabase.trackDao().getTracksIdList())
        }
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
                previewUrl = it.previewUrl,
                isLiked = isFavoriteBoolean(it.trackId, listTrackId)
            )
        }
        return answer.toCollection(ArrayList())
    }

    private fun isFavoriteBoolean(trackId: String, list: List<String>): Boolean {
        var answer = false

        list.forEach { likedTrack ->
            if (likedTrack == trackId) {
                answer = true
            }
        }
        return answer
    }

    companion object {
        private const val USE_CLEAR = "clear"
        private const val USE_READ = "read"
        private const val USE_WRITE = "write"
    }
}
package com.example.playlistmaker.db.data

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.R
import com.example.playlistmaker.db.data.converters.PlayListDbConvertor
import com.example.playlistmaker.db.data.entity.PlayListEntity
import com.example.playlistmaker.db.data.entity.PlaylistTrackEntity
import com.example.playlistmaker.db.domain.PlayListRepository
import com.example.playlistmaker.playlist_create.domain.models.PlayList
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class PlayListRepositoryImpl(
    private val database: AppDatabase,
    private val playListDbConvertor: PlayListDbConvertor,
    private val context: Context
) : PlayListRepository {
    override suspend fun newPlayList(playList: PlayList) =
        database.playListDao().createPlaylist(playListDbConvertor.map(playList))

    override fun listPlayList(): Flow<List<PlayList>> = flow {
        emit(convertIntoPlayList(database.playListDao().getPlaylists()))
    }

    override suspend fun getCountTracks(id: Long): Int {
        return database.playListDao().getTracksCount(id)
    }

    override fun listTrackPlaylist(id: Long): Flow<List<Track>> = flow {
        emit(playListDbConvertor.map(database.playListDao().tracksInPlayList(id)))
    }

    override suspend fun getTimesTracks(id: Long): String {
        var timeInMillis: Long = 0
        database.playListDao().tracksInPlayList(id).forEach { track ->
            timeInMillis += SimpleDateFormat("mm:ss").parse(track.trackTimeMillis)?.time ?: 0
        }
        return SimpleDateFormat("mm", Locale.getDefault()).format(timeInMillis).toString()
    }

    override suspend fun deleteTrack(id: Long) {
        database.playListDao().deleteTrack(id)
    }

    override suspend fun deletePlaylist(id: Long) {
        database.playListDao().deletePlaylistTracks(id)
        database.playListDao().deletePlayList(id)
    }

    override suspend fun shareTracks(id: Long) {
        createIntent(getStringIntent(id))
    }

    override suspend fun editPlayList(playList: PlayList) {
        database.playListDao().createPlaylist(
            PlayListEntity(
                playList.id,
                playList.namePlayList,
                playList.aboutPlayList,
                playList.roadToFileImage
            )
        )
    }

    override suspend fun getPlayList(id: Long): PlayList {
        return playListDbConvertor.map(database.playListDao().getPlayList(id))
    }

    override suspend fun updatePlayList(track: Track, id: Long): Boolean {
        val trackAlreadyExists =
            database.playListDao().findTrack(trackId = track.trackId, playlistId = id).isNotEmpty()
        if (!trackAlreadyExists) {
            database.playListDao().addTracksToPlaylist(
                PlaylistTrackEntity(
                    trackId = track.trackId,
                    playlistId = id,
                    trackName = track.trackName,
                    artistName = track.artistName,
                    trackTimeMillis = track.trackTimeMillis,
                    artworkUrl100 = track.artworkUrl100,
                    collectionName = track.collectionName,
                    releaseDate = track.releaseDate,
                    primaryGenreName = track.primaryGenreName,
                    country = track.country,
                    previewUrl = track.previewUrl
                )
            )
        }
        return !trackAlreadyExists
    }

    private fun convertIntoPlayList(playList: List<PlayListEntity>): List<PlayList> {
        return playList.map { playListEntity -> playListDbConvertor.map(playListEntity) }
    }

    private fun createIntent(textMessage: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textMessage)
            type = "text/plain"
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private suspend fun getStringIntent(id: Long): String {
        val playList = database.playListDao().getPlayList(id)
        var textMessage = context.getString(
            R.string.tracks_share_intent,
            playList.namePlayList,
            playList.aboutPlayList,
            database.playListDao().getTracksCount(id).toString()
        )
        val base = database.playListDao().tracksInPlayList(id).reversed()
            base.forEachIndexed { index, playlistTrackEntity ->
            textMessage += "«${index + 1}. ${playlistTrackEntity.artistName} - ${playlistTrackEntity.trackName} (${playlistTrackEntity.trackTimeMillis})».\n"
        }
        return textMessage
    }
}
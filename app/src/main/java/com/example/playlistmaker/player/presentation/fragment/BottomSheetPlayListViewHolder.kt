package com.example.playlistmaker.player.presentation.fragment

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBsBinding
import com.example.playlistmaker.playlist_create.domain.models.PlayList


class BottomSheetPlayListViewHolder(private val binding: PlaylistItemBsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PlayList) {
        with(binding) {
            tvNamePlaylistBS.text = item.namePlayList
            tvCountTrackBS.text = item.count.toString() + " " + formatTrack(item.count.toInt())
            if (item.roadToFileImage.isNotEmpty()) {
                ivImageBS.setImageURI(item.roadToFileImage.toUri())
            } else {
                ivImageBS.setImageResource(R.drawable.placeholder_ic)
            }
        }
    }

    private fun formatTrack(numberTracks: Int): String {
        if (numberTracks % 10 == 0) {
            return "треков"
        }
        if (numberTracks % 10 == 1 && !(numberTracks % 100 >= 11 && numberTracks % 100 <= 19)) {
            return "трек"
        }
        if (numberTracks % 10 < 5 && !(numberTracks % 100 >= 11 && numberTracks % 100 <= 19)) {
            return "трека"
        } else {
            return "треков"
        }
    }
}
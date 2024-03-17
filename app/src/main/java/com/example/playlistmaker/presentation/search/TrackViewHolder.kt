package com.example.playlistmaker.presentation.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.RADIUS_CUT_IMAGE
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.dpToPx

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val artworkUrl: ImageView = itemView.findViewById(R.id.ivArtworkUrl100)
    private val trackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val artistName: TextView = itemView.findViewById(R.id.tvArtistName)
    private val trackTime: TextView = itemView.findViewById(R.id.tvTrackTime)

    fun bind(item: Track) {

        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = item.trackTimeMillis

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder_ic)
            .centerInside()
            .transform(RoundedCorners(dpToPx(RADIUS_CUT_IMAGE, itemView.context)))
            .into(artworkUrl)
    }
}
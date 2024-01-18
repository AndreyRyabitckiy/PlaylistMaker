package com.example.playlistmaker

import android.view.RoundedCorner
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.EventListener
import java.util.Locale

class TrackViewHolder(itemViev: View) : RecyclerView.ViewHolder(itemViev) {

    private val artworkUrl: ImageView = itemViev.findViewById(R.id.ivArtworkUrl100)
    private val trackName: TextView = itemViev.findViewById(R.id.tvTrackName)
    private val artistName: TextView = itemViev.findViewById(R.id.tvArtistName)
    private val trackTime: TextView = itemViev.findViewById(R.id.tvTrackTime)
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }


    fun bind(item: Track) {

        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = dateFormat.format(item.trackTimeMillis.toLong())

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder_ic)
            .centerInside()
            .transform(RoundedCorners(10))
            .into(artworkUrl)
    }
}
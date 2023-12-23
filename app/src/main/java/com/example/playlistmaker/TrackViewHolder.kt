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

class TrackViewHolder(itemViev: View):RecyclerView.ViewHolder(itemViev) {

    private val artworkUrl: ImageView = itemViev.findViewById(R.id.artworkUrl100)
    private val trackName: TextView = itemViev.findViewById(R.id.trackName)
    private val artistName: TextView = itemViev.findViewById(R.id.artistName)
    private val trackTime: TextView = itemViev.findViewById(R.id.trackTime)

    fun bind (item: Track) {
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = item.trackTime
        Glide.with(itemView)

            .load(item.artworkUrl100)
            .placeholder(R.drawable.no_internet_ic)
            .centerInside()
            .transform(RoundedCorners(10))
            .into(artworkUrl)


    }
}
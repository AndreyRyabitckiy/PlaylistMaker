package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class TrackAdapter(
    private val data: ArrayList<Track>,
    private val onItemClickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener { onItemClickListener?.onItemClick(data[position]) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

}
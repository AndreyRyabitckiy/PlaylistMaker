package com.example.playlistmaker.search.presentation.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.search.domain.models.Track

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var data: List<Track> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onClick: (Track) -> Unit = {}

    var onClickLong: (Track) -> Unit = {}


    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener { onClick(data[position]) }
        holder.itemView.setOnLongClickListener {
            onClickLong(data[position])
            true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackItemBinding.inflate(view, parent, false))
    }
}
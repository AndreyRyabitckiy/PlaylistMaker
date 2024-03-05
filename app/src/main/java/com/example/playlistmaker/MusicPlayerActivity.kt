package com.example.playlistmaker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class MusicPlayerActivity : AppCompatActivity() {

    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        val backButton = findViewById<ImageView>(R.id.backIv)
        val musicImage = findViewById<ImageView>(R.id.musicImageIv)
        val nameMusic = findViewById<TextView>(R.id.nameMusicTv)
        val groupName = findViewById<TextView>(R.id.groupNameTv)
        val timeMusic = findViewById<TextView>(R.id.timeMusicAnswerTv)
        val groupMusic = findViewById<TextView>(R.id.groupMusicAnswerTv)
        val ear = findViewById<TextView>(R.id.earAnswerTv)
        val typeMusic = findViewById<TextView>(R.id.typeMusicAnswerTv)
        val country = findViewById<TextView>(R.id.countryAnswerTv)
        val textGroup = findViewById<Group>(R.id.textGroup)

        backButton.setOnClickListener {
            finish()
        }

        intent.parcelable<Track>(TRACK_KEY)?.let { track ->

            nameMusic.text = track.trackName
            groupName.text = track.artistName
            timeMusic.text = dateFormat.format(track.trackTimeMillis.toLong())
            if (track.collectionName == null) {
                textGroup.isVisible = false
            } else {
                groupMusic.text = track.collectionName
            }
            ear.text = getFormattedDate(track.releaseDate)
            typeMusic.text = track.primaryGenreName
            country.text = track.country
            val urlImage = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

            Glide.with(this)
                .load(urlImage)
                .placeholder(R.drawable.placeholder_ic)
                .centerInside()
                .transform(RoundedCorners(radiusCutImage))
                .into(musicImage)
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

    companion object {
        const val TRACK_KEY = "TRACK"
    }
}



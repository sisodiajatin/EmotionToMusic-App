package com.example.emotionmusicrecommender

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Toolbar back arrow
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        // Mood + emoji
        val emojiMap = mapOf(
            "happy" to "😄", "sad" to "😢", "neutral" to "😐",
            "angry" to "😠", "surprise" to "😲",
            "fear" to "😱", "disgust" to "🤢"
        )
        val mood = intent.getStringExtra("mood")?.lowercase() ?: "neutral"
        val moodText = "${emojiMap[mood] ?: ""} ${mood.replaceFirstChar { it.uppercase() }}"
        findViewById<TextView>(R.id.mood_text).text =
            getString(R.string.mood_prefix, moodText)

        // Song & artist
        findViewById<TextView>(R.id.song_name_text).text =
            intent.getStringExtra("song_name") ?: ""
        findViewById<TextView>(R.id.artist_text).text =
            intent.getStringExtra("artist") ?: ""

        // Album art via Glide
        val albumUrl = intent.getStringExtra("album_url")
        albumUrl?.takeIf { it.isNotEmpty() }?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.mipmap.ic_launcher)
                .into(findViewById<ImageView>(R.id.album_art))
        }

        // Open in Spotify
        val trackId = intent.getStringExtra("song_id") ?: ""
        findViewById<MaterialButton>(R.id.open_spotify)
            .setOnClickListener {
                startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com/track/$trackId"))
                )
            }

        // Retake photo
        findViewById<MaterialButton>(R.id.retake_photo)
            .setOnClickListener { finish() }
    }
}

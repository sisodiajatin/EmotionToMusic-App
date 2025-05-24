package com.example.emotionmusicrecommender

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * The top‑level response from your Flask server.
 */
data class DetectResponse(
    @SerializedName("dominant_emotion")
    val dominantEmotion: String,

    // we only care about this piece of the JSON
    @SerializedName("recommended_song")
    val recommendedSong: RecommendedSong
)

/**
 * Holds the 3 fields your backend is returning
 * under the "recommended_song" key.
 */
data class RecommendedSong(
    @SerializedName("song_name")
    val songName: String,

    @SerializedName("song_id")
    val songId: String,

    @SerializedName("artist")
    val artist: String
)

interface EmotionApiService {
    @Multipart
    @POST("detect_emotion")
    suspend fun uploadPhoto(
        @Part image: MultipartBody.Part
    ): DetectResponse
}

package com.example.emotionmusicrecommender

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_PERMS = 10
        private val PERMS = arrayOf(Manifest.permission.CAMERA)
        private const val BASE_URL = "https://c5f0-34-143-190-167.ngrok-free.app/"
        private const val TAG = "MainActivity"
    }

    private lateinit var previewView: PreviewView
    private lateinit var scrim: View
    private lateinit var overlayFrame: View
    private lateinit var progressBar: ProgressBar
    private lateinit var captureFab: FloatingActionButton
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previewView    = findViewById(R.id.viewFinder)
        scrim          = findViewById(R.id.scrim)
        overlayFrame   = findViewById(R.id.overlay_frame)
        progressBar    = findViewById(R.id.progress_bar)
        captureFab     = findViewById(R.id.camera_capture_button)

        // keep scrim hidden until we capture
        scrim.visibility = View.GONE

        if (PERMS.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, PERMS, REQUEST_PERMS)
        }

        captureFab.setOnClickListener { takePhoto() }
    }

    private fun startCamera() {
        ProcessCameraProvider.getInstance(this).also { future ->
            future.addListener({
                cameraProvider = future.get()
                val preview = Preview.Builder()
                    .build()
                    .also { it.setSurfaceProvider(previewView.surfaceProvider) }

                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                    preview,
                    imageCapture
                )
            }, ContextCompat.getMainExecutor(this))
        }
    }

    private fun takePhoto() {
        scrim.visibility    = View.VISIBLE
        scrim.isClickable   = false
        captureFab.isEnabled= false
        progressBar.visibility = View.VISIBLE

        val file = File(externalCacheDir, "photo_${System.currentTimeMillis()}.jpg")
        val options = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(
            options,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Capture failed", Toast.LENGTH_SHORT).show()
                        restoreUi()
                    }
                }

                override fun onImageSaved(result: ImageCapture.OutputFileResults) {
                    cameraProvider.unbindAll()
                    uploadPhoto(file)
                }
            }
        )
    }

    private fun uploadPhoto(file: File) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
                .create(EmotionApiService::class.java)

            val mime = "image/jpeg".toMediaType()
            val body = file.asRequestBody(mime)
            val part = MultipartBody.Part.createFormData("image", file.name, body)

            val resp = api.uploadPhoto(part)
            Log.d(TAG, "Got response: $resp")

            withContext(Dispatchers.Main) {
                Intent(this@MainActivity, ResultActivity::class.java).apply {
                    putExtra("mood", resp.dominantEmotion)
                    putExtra("song_name", resp.recommendedSong.songName)
                    putExtra("artist", resp.recommendedSong.artist)
                    putExtra("song_id", resp.recommendedSong.songId)
                    startActivity(this)
                }
                restoreUi()
            }
        } catch (e: Exception) {
            Log.e(TAG, "uploadPhoto error", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@MainActivity,
                    "Upload failed: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
                restoreUi()
            }
        }
    }

    private fun restoreUi() {
        scrim.visibility    = View.GONE
        scrim.isClickable   = true
        captureFab.isEnabled= true
        progressBar.visibility = View.GONE
        startCamera()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMS &&
            grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        ) {
            startCamera()
        } else {
            finish()
        }
    }
}

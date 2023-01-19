package com.practice.getup.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practice.getup.R
import com.practice.getup.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadVideo()

    }



    companion object{
        const val VIDEO_ID = "VIDEO_ID"
    }

    private fun loadVideo(){

        val mediaController = android.widget.MediaController(this)
        mediaController.setAnchorView(binding.videoView1)

        val intRes = intent.getIntExtra(VIDEO_ID, R.raw.pushups_video)
        val offlineVideoUri: Uri =
            Uri.parse("android.resource://$packageName/${intRes}")

        with(binding.videoView1) {
            setMediaController(mediaController)
            setVideoURI(offlineVideoUri)
            requestFocus()
            start()
        }
    }
}
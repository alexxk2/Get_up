package com.practice.getup.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.practice.getup.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var intRes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            intRes = it.getInt(INT_RES)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadVideo()
    }

    private fun loadVideo(){

        val mediaController = android.widget.MediaController(context)
        mediaController.setAnchorView(binding.videoView1)

        val offlineVideoUri: Uri =
            Uri.parse("android.resource://${context?.packageName}/${intRes}")

        with(binding.videoView1) {
            setMediaController(mediaController)
            setVideoURI(offlineVideoUri)
            requestFocus()
            start()
        }
    }

    companion object{
       const val INT_RES = "intres"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
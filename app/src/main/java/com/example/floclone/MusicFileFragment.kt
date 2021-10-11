package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.floclone.databinding.FragmentMusicFileBinding
import com.example.floclone.databinding.FragmentSavedSongBinding

class MusicFileFragment : Fragment() {
    lateinit var binding: FragmentMusicFileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMusicFileBinding.inflate(inflater, container, false)


        return binding.root
    }

}
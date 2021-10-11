package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.floclone.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    lateinit var binding : FragmentSongBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var sign = 0

        binding = FragmentSongBinding.inflate(inflater, container, false)

        binding.toggleFavorMixIv.setOnClickListener {
            if(sign != 0) {
                setToggle(true)
                sign = 0
            }
            else {
                setToggle(false)
                sign = 1
            }
        }

        binding.songListTitle1Tv.setOnClickListener {
            makeToastMessage(binding.songListTitle1Tv.text)
        }
        binding.songListTitle2Tv.setOnClickListener {
            makeToastMessage(binding.songListTitle2Tv.text)

        }
        binding.songListTitle3Tv.setOnClickListener {
            makeToastMessage(binding.songListTitle3Tv.text)
        }
        binding.songListTitle4Tv.setOnClickListener {
            makeToastMessage(binding.songListTitle4Tv.text)
        }
        binding.songListTitle5Tv.setOnClickListener {
            makeToastMessage(binding.songListTitle5Tv.text)
        }

        return binding.root
    }

    fun makeToastMessage(text : CharSequence) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }
    fun setToggle(isOn : Boolean) {
        if(isOn)
            binding.toggleFavorMixIv.setBackgroundResource(R.drawable.btn_toggle_off)
        else
            binding.toggleFavorMixIv.setBackgroundResource(R.drawable.btn_toggle_on)
    }
}
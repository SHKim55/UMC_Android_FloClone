package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.floclone.databinding.FragmentSongBinding
import com.google.gson.Gson

class SongFragment(private var track : ArrayList<Song>) : Fragment() {
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

        //더미데이터와 Adapter 연결
        val songRVAdapter = SongRVAdapter(track)
        //Adapter - RV 연결
        binding.songAlbumTrackRecyclerview.adapter = songRVAdapter

        songRVAdapter.setMyItemClickListener(object: SongRVAdapter.SongItemClickListener {
            override fun onItemClick(song : Song) { makeToastMessage(song.title) }
            override fun onMoreButtonClick(index : Int) { songRVAdapter.removeItem(index) }
        })

        //레이아웃 매니저 추가
        binding.songAlbumTrackRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

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
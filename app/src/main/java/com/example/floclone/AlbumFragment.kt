package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.floclone.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment () : Fragment () {

    lateinit var binding: FragmentAlbumBinding
    val information = arrayListOf("수록곡", "상세정보", "영상")
    private var gson: Gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        //JSON Object 받아오기
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)

        var scaleText = ""
        when(album.albumScale) {
            0 -> { scaleText = "정규" }
            1 -> { scaleText = "미니" }
            2 -> { scaleText = "싱글" }
        }

        binding.albumTitleTv.text = album.title
        binding.albumSingerNameTv.text = album.singer
        binding.albumBackIv.setImageResource(album.backgroundImageRes)
        binding.albumDescriptionTv.text = album.releaseDate + " | " + scaleText + " | " + album.genre

        binding.btnAlbumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        val albumAdapter = AlbumViewpagerAdapter(this, album.songs)
        binding.albumContentVp.adapter = albumAdapter

        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {
                tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }
}
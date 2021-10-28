package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.floclone.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment (paramAlbum : Album) : Fragment () {

    lateinit var binding: FragmentAlbumBinding
    val information = arrayListOf("수록곡", "상세정보", "영상")
    val album = paramAlbum

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        var scaleName = ""
        binding.albumTitleTv.text = album.title
        binding.albumSingerNameTv.text = album.singer
        binding.albumBackIv.setImageResource(album.backgroundImageRes)

        when(album.albumScale) {
            0 -> { scaleName = "정규" }
            1 -> { scaleName = "미니" }
            2 -> { scaleName = "싱글" }
        }
        binding.albumDescriptionTv.text = album.releaseDate + " | " + scaleName + " | " + album.genre

        binding.btnAlbumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        val albumAdapter = AlbumViewpagerAdapter(this)
        binding.albumContentVp.adapter = albumAdapter

        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {
            tab, position ->
                tab.text = information[position]
        }.attach()

        return binding.root
    }
}
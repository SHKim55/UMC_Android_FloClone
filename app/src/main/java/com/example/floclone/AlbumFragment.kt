package com.example.floclone

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.floclone.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment () {
    lateinit var binding: FragmentAlbumBinding
    private lateinit var songDB : SongDatabase
    private val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(context as MainActivity)!!

        initAlbum()
        return binding.root
    }

    private fun initAlbum() {
        val albumId = requireArguments().getInt("albumId")
        Log.d("albumId", albumId.toString())
        val album = songDB.AlbumDao().getAlbum(albumId)
        isLiked = isLikeAlbum(album.id)

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

        if(isLiked)
            binding.btnAlbumLike.setImageResource(R.drawable.ic_my_like_on)
        else
            binding.btnAlbumLike.setImageResource(R.drawable.ic_my_like_off)

        val albumAdapter = AlbumViewpagerAdapter(this, songDB.SongDao().getSongsInAlbum(albumId) as ArrayList<Song>)
        binding.albumContentVp.adapter = albumAdapter

        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {
                tab, position ->
            tab.text = information[position]
        }.attach()

        initOnClickListener(album)
    }

    private fun initOnClickListener(album : Album) {
        val userId : Int = getJwtId()

        binding.btnAlbumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        binding.btnAlbumLike.setOnClickListener {
            if(isLiked) {
                binding.btnAlbumLike.setImageResource(R.drawable.ic_my_like_off)
                dislikeAlbum(userId, album.id)
            } else {
                binding.btnAlbumLike.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }
        }
    }

    private fun likeAlbum(userId : Int, albumId : Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.AlbumDao().likeAlbum(like)
    }

    private fun dislikeAlbum(userId : Int, albumId : Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        songDB.AlbumDao().disLikeAlbum(userId, albumId)
    }

    private fun isLikeAlbum(albumId: Int) : Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwtId()

        val likeId : Int? = songDB.AlbumDao().isLikeAlbum(userId, albumId)
        return likeId != null
    }

    private fun getJwtId() : Int {
        val sharedPreference = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return sharedPreference!!.getInt("jwtId", 0)
    }
}
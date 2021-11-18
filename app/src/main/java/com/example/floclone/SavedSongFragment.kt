package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.floclone.databinding.FragmentSavedSongBinding

class SavedSongFragment(private val songs : ArrayList<Song>) : Fragment() {
    lateinit var binding : FragmentSavedSongBinding
    lateinit var songDB : SongDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSavedSongBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.savedSongRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val savedSongRVAdapter = SavedSongRVAdapter()

        binding.savedSongRecyclerview.adapter = savedSongRVAdapter
        savedSongRVAdapter.addSongs(songDB.SongDao().getLikedSongs(true) as ArrayList<Song>)

        savedSongRVAdapter.setSavedSongItemClickListener(object: SavedSongRVAdapter.SavedSongItemClickListener {
            override fun onMoreButtonClick(songId : Int) {
                songDB.SongDao().updateIsLikeById(false, songId)
                (context as MainActivity).song.isLike = false
            }
        })
    }

}
package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.floclone.databinding.FragmentSavedSongBinding
import com.example.floclone.databinding.FragmentSongBinding

class SavedSongFragment(private val songs : ArrayList<Song>) : Fragment() {
    lateinit var binding : FragmentSavedSongBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSavedSongBinding.inflate(inflater, container, false)

        val savedSongRVAdapter = SavedSongRVAdapter(songs)
        binding.savedSongRecyclerview.adapter = savedSongRVAdapter

        savedSongRVAdapter.setSavedSongItemClickListener(object: SavedSongRVAdapter.SavedSongItemClickListener {
            override fun onMoreButtonClick(index : Int) { savedSongRVAdapter.removeItem(index) }
        })

        binding.savedSongRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }

}
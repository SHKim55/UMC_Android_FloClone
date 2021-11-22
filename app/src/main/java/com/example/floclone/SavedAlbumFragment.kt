package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.floclone.databinding.FragmentSavedAlbumBinding

class SavedAlbumFragment(loginId : Int) : Fragment() {
    lateinit var binding : FragmentSavedAlbumBinding
    lateinit var songDB : SongDatabase
    private val userId = loginId

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedAlbumBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.savedAlbumRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val savedAlbumRVAdapter = SavedAlbumRVAdapter(userId)

        binding.savedAlbumRecyclerview.adapter = savedAlbumRVAdapter
        savedAlbumRVAdapter.addAlbums(songDB.AlbumDao().getLikedAlbums(userId) as ArrayList<Album>)

        savedAlbumRVAdapter.setSavedAlbumItemClickListener(object: SavedAlbumRVAdapter.SavedAlbumItemClickListener {
            override fun onMoreButtonClick(userId : Int, albumId : Int) { songDB.AlbumDao().disLikeAlbum(userId, albumId) }
        })
    }
}
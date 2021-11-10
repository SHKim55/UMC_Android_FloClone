package com.example.floclone

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumViewpagerAdapter (fragment: Fragment, private val songs: ArrayList<Song>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val songs = songs

        return when(position) {
            0 -> SongFragment(songs)
            1 -> DetailFragment()
            else -> VideoFragment()
        }
    }
}
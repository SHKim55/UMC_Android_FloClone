package com.example.floclone

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerViewpagerAdapter (fragment : Fragment, private val id : Int, private val songs : ArrayList<Song>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SavedSongFragment(songs)
            1 -> SavedAlbumFragment(id)
            else -> MusicFileFragment()
        }
    }

}
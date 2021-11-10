package com.example.floclone

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerViewpagerAdapter (fragment : Fragment, private val songs : ArrayList<Song>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SavedSongFragment(songs)
            else -> MusicFileFragment()
        }
    }

}
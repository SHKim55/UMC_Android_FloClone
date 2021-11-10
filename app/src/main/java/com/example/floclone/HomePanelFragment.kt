package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.floclone.databinding.FragmentHomePanelBinding

class HomePanelFragment(private val panelData: Panel) : Fragment() {
    lateinit var binding : FragmentHomePanelBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomePanelBinding.inflate(inflater, container, false)

        binding.textTitle.setText(panelData.titleText)
        binding.imgBackgroundIv.setImageResource(panelData.backgroundImgRes)
        binding.imgAlbumEx1Iv.setImageResource(panelData.albumImg1)
        binding.imgAlbumEx2Iv.setImageResource(panelData.albumImg2)
        binding.textAlbumEx1MusicTitle.setText(panelData.albumTitle1)
        binding.textAlbumEx2MusicTitle.setText(panelData.albumTitle2)
        binding.textAlbumEx1Musician.setText(panelData.albumSinger1)
        binding.textAlbumEx2Musician.setText(panelData.albumSinger2)
        binding.textRecentDate.setText(panelData.updateDate)
        binding.textMusicCount.setText("총 ${panelData.songNum}곡")

        return binding.root
    }
}
package com.example.floclone

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.floclone.databinding.FragmentHomeBinding
import com.google.gson.Gson


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var albumDB : ArrayList<Album>
    private var albumData =  ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        albumDB = (context as MainActivity).albums
        //데이터 리스트 생성
        albumData.apply {
            for(i in 0 until albumDB.size) {
                add(albumDB[i])
            }
        }

        //더미데이터와 Adapter 연결
        val albumRVAdapter = AlbumRVAdapter(albumData)
        //Adapter - RV 연결
        binding.homeTodayMusicAlbumRecyclerview.adapter = albumRVAdapter

        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onPlayButtonClick(album : Album, index : Int) {
                val nextSong = album.songs.get(0)
                (context as MainActivity).albumIndex = index
                (context as MainActivity).songIndex = 0
                changePlayer(nextSong)
            }
        })

        //레이아웃 매니저 추가
        binding.homeTodayMusicAlbumRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // 홈 패널 구성
        val panel1 = Panel("빌보드를 휩쓸어버린 그 노래", R.drawable.img_background_4_x_1, R.drawable.img_album_butter, R.drawable.img_album_dynamite,
            "Butter", "Dynamite", "방탄소년단", "방탄소년단", "2021.09.10", 10)

        val panel2 = Panel("과제할 때 듣는 신나는 노래", R.drawable.img_background_2, R.drawable.img_album_lilac, R.drawable.img_album_traffic_light,
            "LILAC", "신호등", "아이유 (IU)", "이무진", "2021.11.12", 5)

        val panelAdapter = HomePanelViewpagerAdapter(this)
        panelAdapter.addFragment(HomePanelFragment(panel1))
        panelAdapter.addFragment(HomePanelFragment(panel2))

        binding.homePanelVp.adapter = panelAdapter
        binding.homePanelVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        // 광고 배너 패널 구성
        val bannerAdapter = BannerViewpagerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }

    private fun changeAlbumFragment(album : Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

    private fun changePlayer(song : Song) {
        (context as MainActivity).song = song
        (context as MainActivity).setMiniPlayer(song)
    }
}
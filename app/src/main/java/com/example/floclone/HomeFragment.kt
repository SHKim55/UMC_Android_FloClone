package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.floclone.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.imgAlbumEx1RecommendedIv.setOnClickListener {
            (context as MainActivity).songIndex = 0
            val index = (context as MainActivity).songIndex
            val tempTitle = (context as MainActivity).songs.get(index).title
            val tempSinger = (context as MainActivity).songs.get(index).singer
            val tempAlbumCoverRes = (context as MainActivity).songs.get(index).backgroundImageRes
            val album = Album("IU 5th Album 'LILAC'", tempSinger, "2021.03.25", "가요/댄스", tempAlbumCoverRes, 0)

            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment(album))
                .commitAllowingStateLoss()

            (context as MainActivity).binding.mainMiniplayerTitleTv.text = tempTitle
            (context as MainActivity).binding.mainMiniplayerSingerTv.text = tempSinger
            (context as MainActivity).binding.mainMiniplayerSb.progress = 0
            (context as MainActivity).miniPlayer.second = 0
        }

        binding.imgAlbumEx2RecommendedIv.setOnClickListener {
            (context as MainActivity).songIndex = 1
            val index = (context as MainActivity).songIndex
            val tempTitle = (context as MainActivity).songs.get(index).title
            val tempSinger = (context as MainActivity).songs.get(index).singer
            val tempAlbumCoverRes = (context as MainActivity).songs.get(index).backgroundImageRes
            val album = Album("항해", tempSinger, "2019.09.25", "가요/락", tempAlbumCoverRes, 0)

            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment(album))
                .commitAllowingStateLoss()

            (context as MainActivity).binding.mainMiniplayerTitleTv.text = tempTitle
            (context as MainActivity).binding.mainMiniplayerSingerTv.text = tempSinger
            (context as MainActivity).binding.mainMiniplayerSb.progress = 0
            (context as MainActivity).miniPlayer.second = 0
        }

        binding.imgAlbumEx3RecommendedIv.setOnClickListener {
            (context as MainActivity).songIndex = 2
            val index = (context as MainActivity).songIndex
            val tempTitle = (context as MainActivity).songs.get(index).title
            val tempSinger = (context as MainActivity).songs.get(index).singer
            val tempAlbumCoverRes = (context as MainActivity).songs.get(index).backgroundImageRes
            val album = Album("신호등", tempSinger, "2021.05.14", "가요/락", tempAlbumCoverRes, 2)

            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment(album))
                .commitAllowingStateLoss()

            (context as MainActivity).binding.mainMiniplayerTitleTv.text = tempTitle
            (context as MainActivity).binding.mainMiniplayerSingerTv.text = tempSinger
            (context as MainActivity).binding.mainMiniplayerSb.progress = 0
            (context as MainActivity).miniPlayer.second = 0
        }

        binding.imgAlbumEx4RecommendedIv.setOnClickListener {
            (context as MainActivity).songIndex = 3
            val index = (context as MainActivity).songIndex
            val tempTitle = (context as MainActivity).songs.get(index).title
            val tempSinger = (context as MainActivity).songs.get(index).singer
            val tempAlbumCoverRes = (context as MainActivity).songs.get(index).backgroundImageRes
            val album = Album("신호등", tempSinger, "2021.05.14", "가요/락", tempAlbumCoverRes, 2)

            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment(album))
                .commitAllowingStateLoss()

            (context as MainActivity).binding.mainMiniplayerTitleTv.text = tempTitle
            (context as MainActivity).binding.mainMiniplayerSingerTv.text = tempSinger
            (context as MainActivity).binding.mainMiniplayerSb.progress = 0
            (context as MainActivity).miniPlayer.second = 0
        }

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
}
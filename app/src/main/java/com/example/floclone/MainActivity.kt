package com.example.floclone

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.floclone.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var miniPlayer : MiniPlayer
    private val handler = Handler(Looper.getMainLooper())
    var songs = ArrayList<Song>()
    var songIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 곡 정보 입력
        songs.add(Song("LILAC", "아이유(IU)", "내리는 꽃가루에\n눈이 따끔해", R.drawable.img_album_lilac, 235, false))
        songs.add(Song("달", "AKMU(악동뮤지션)", "유난히 밝은 달\n거대한 원형 속에 보이네 너의 미소", R.drawable.img_album_sailing, 242, false))
        songs.add(Song("신호등", "이무진", "이제야 목적지를 정했지만\n가려한 날 막아서네 난 갈 길이 먼데", R.drawable.img_album_traffic_light, 232, false))

        // 미니플레이어 설정
        binding.mainMiniplayerTitleTv.text = songs.get(songIndex).title
        binding.mainMiniplayerSingerTv.text = songs.get(songIndex).singer
        binding.mainMiniplayerSb.progress = 0
        miniPlayer = MiniPlayer(songs.get(songIndex).playTime, songs.get(songIndex).isPlaying)
        miniPlayer.start()

        binding.mainPlayerLayout.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            val selectedSong = songs.get(songIndex)
            intent.putExtra("title", selectedSong.title)
            intent.putExtra("singer", selectedSong.singer)
            intent.putExtra("lyrics", selectedSong.lyrics)
            intent.putExtra("backgroundImageRes", selectedSong.backgroundImageRes)
            intent.putExtra("playTime", selectedSong.playTime)
            intent.putExtra("isPlaying", selectedSong.isPlaying)
            startActivity(intent)
        }

        binding.mainMiniplayerBtn.setOnClickListener {
            miniPlayer.isPlaying = true

            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        }

        binding.mainPauseBtn.setOnClickListener {
            miniPlayer.isPlaying = false
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }

        initNavigation()

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

    }

    inner class MiniPlayer(private val playTime : Int, var isPlaying: Boolean) : Thread() {
        var second = 0

        override fun run() {
            try {
                while (true) {
                    if (second >= playTime)
                        break

                    if (isPlaying) {
                        sleep(1000)
                        second++
                        handler.post {
                            binding.mainMiniplayerSb.progress = second * 1000 / playTime
                        }
                    }
                }
            } catch (e : InterruptedException) {
                Log.d("Interrupt", "Thread가 종료되었습니다.")
            }
        }
    }
}


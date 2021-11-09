package com.example.floclone

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.floclone.databinding.ActivityMainBinding
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    lateinit var miniPlayer : MiniPlayer
    private var mediaPlayer : MediaPlayer? = null
    private var gson : Gson = Gson()
    var song : Song = Song()
    var songs = ArrayList<Song>()
    var songIndex = 0
    var playPosition : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 곡 정보 입력
        songs.add(Song("LILAC", "아이유(IU)", "내리는 꽃가루에\n눈이 따끔해", R.drawable.img_album_lilac, 235, false, 0, "lilac_iu", 0))
        songs.add(Song("달", "AKMU(악동뮤지션)", "유난히 밝은 달\n거대한 원형 속에 보이네 너의 미소", R.drawable.img_album_sailing, 242, false, 0, "moon_akmu", 0))
        songs.add(Song("신호등", "이무진", "이제야 목적지를 정했지만\n가려한 날 막아서네 난 갈 길이 먼데", R.drawable.img_album_traffic_light, 232, false, 0, "traffic_light_lee_mujin", 0))
        songs.add(Song("strawberry moon", "아이유(IU)", "달이 익어가니 서둘러 젊은 피야\n민들레 한 송이 들고", R.drawable.img_album_strawberry, 205, false, 0, "strawberry_moon_iu", 0))
        //song = songs.get(songIndex)   // 현재 재생중인 곡

        binding.mainPlayerLayout.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            val selectedSong = songs.get(songIndex)
            intent.putExtra("title", selectedSong.title)
            intent.putExtra("singer", selectedSong.singer)
            intent.putExtra("lyrics", selectedSong.lyrics)
            intent.putExtra("backgroundImageRes", selectedSong.backgroundImageRes)
            intent.putExtra("playTime", selectedSong.playTime)
            intent.putExtra("isPlaying", selectedSong.isPlaying)
            intent.putExtra("second", selectedSong.second)
            intent.putExtra("music", selectedSong.music)
            intent.putExtra("playPos", selectedSong.playPos)

            mediaPlayer?.stop()
            miniPlayer.interrupt()
            startActivity(intent)
        }

        binding.mainMiniplayerBtn.setOnClickListener {
            miniPlayer.isPlaying = true
            song.isPlaying = true
            mediaPlayer?.start()
            mediaPlayer?.seekTo(song.playPos)

            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        }

        binding.mainPauseBtn.setOnClickListener { onPause() }

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

    override fun onStart() {
        super.onStart()

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val jsonSongData = sharedPreferences.getString("song", null)
        if(jsonSongData == null) {
            song = songs.get(0)
        } else {
            song = gson.fromJson(jsonSongData, Song::class.java)

            for(i in 0 until songs.size) {
                if(song.title.equals(songs[i].title)) {
                    songIndex = i
                    songs[i] = song
                }
            }
        }
        setMiniPlayer(song)
    }

    override fun onPause() {
        super.onPause()
        this.mediaPlayer?.pause()   // 미디어플레이어 중지
        miniPlayer.isPlaying = false // Thread 중지
        song.isPlaying = false
        song.playPos = mediaPlayer?.currentPosition!!
        song.second = (binding.mainMiniplayerSb.progress * song.playTime) / 1000

        // 최근 재생 위치 저장
        playPosition = mediaPlayer?.currentPosition!!

        binding.mainMiniplayerBtn.visibility = View.VISIBLE
        binding.mainPauseBtn.visibility = View.GONE   // 정지 상태일때의 이미지로 전환

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = gson.toJson(song)
        editor.putString("song", json)

        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        miniPlayer.interrupt()
        mediaPlayer?.release()   // 미디어 플레이어가 갖고 있던 리소스 해제 (song)
        mediaPlayer = null       // 미디어 플레이어 해제
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()
    }

    fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerSb.progress = song.second * 1000 / song.playTime

        mediaPlayer?.release()   // 미디어 플레이어 초기화

        song.isPlaying = false
        miniPlayer = MiniPlayer(song.playTime, song.isPlaying, song.second)

        val musicFile = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, musicFile)
        miniPlayer.start()
    }

    inner class MiniPlayer(private val playTime : Int, var isPlaying: Boolean, var savedSecond : Int) : Thread() {
        var second = savedSecond

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
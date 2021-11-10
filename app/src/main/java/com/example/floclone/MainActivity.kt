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

    // 음악 정보 저장 DB
    var albums = ArrayList<Album>()
    // 미니플레이어 재생 음악
    var song : Song = Song()
    // 선택된 음악의 index
    var albumIndex = 0
    var songIndex = 0
    var playPosition : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 곡 정보 입력
        loadMusicData()

        // 현재 재생중인 곡
        song = albums[albumIndex].songs.get(songIndex)

        binding.mainPlayerLayout.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            val selectedSong = song
            intent.putExtra("title", selectedSong.title)
            intent.putExtra("singer", selectedSong.singer)
            intent.putExtra("lyrics", selectedSong.lyrics)
            intent.putExtra("backgroundImageRes", selectedSong.backgroundImageRes)
            intent.putExtra("length", selectedSong.length)
            intent.putExtra("filename", selectedSong.filename)
            intent.putExtra("trackNum", selectedSong.trackNum)
            intent.putExtra("isPlaying", selectedSong.isPlaying)
            intent.putExtra("second", selectedSong.second)
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

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()
    }

    private fun loadMusicData() {
        val songs1 = ArrayList<Song>()
        songs1.add(Song("라일락", "아이유(IU)", "내리는 꽃가루에\n눈이 따끔해", R.drawable.img_album_lilac, 235, "lilac_iu", 1, false, 0, 0))
        songs1.add(Song("Flu", "아이유(IU)", "Doc, I'm feeling bad\n미열이 흐르고 또 어질어질해", R.drawable.img_album_lilac, 188, "flu_iu", 2, false, 0, 0))
        songs1.add(Song("Coin", "아이유(IU)", "강자에게 더 세게 I love gamble\n과감할수록 신세계 on my table", R.drawable.img_album_lilac, 193, "coin_iu", 3, false, 0, 0))
        val album1 = Album("IU 5th Album 'LILAC'", "아이유(IU)", "2021.03.25", "가요 / 댄스", R.drawable.img_album_lilac, 0, songs1)

        val songs2 = ArrayList<Song>()
        songs2.add(Song("뱃노래", "AKMU(악뮤)", "귓가에 넘치는 바다\n눈을 감고 느낀다",  R.drawable.img_album_sailing, 138, "chantey_akmu", 1, false, 0, 0))
        songs2.add(Song("물 만난 물고기", "AKMU(악뮤)", "한바탕 휩쓸고 간 폭풍의 잔해 속에\n언제 그랬냐는 듯 잔잔한 파도",  R.drawable.img_album_sailing, 246, "fish_with_water_akmu", 2, false, 0, 0))
        songs2.add(Song("어떻게 이별까지 사랑하겠어, 널 사랑하는 거지", "AKMU(악뮤)", "일부러 몇 발자국 물러나\n내가 없이 혼자 걷는 널 바라본다",  R.drawable.img_album_sailing, 216, "how_come_I_love_you_akmu", 3, false, 0, 0))
        songs2.add(Song("달", "AKMU(악동뮤지션)", "유난히 밝은 달\n거대한 원형 속에 보이네 너의 미소", R.drawable.img_album_sailing, 242, "moon_akmu", 4, false, 0, 0))
        val album2 = Album("항해", "AKMU(악뮤)", "2019.09.25", "가요 / 락", R.drawable.img_album_sailing, 0, songs2)

        val songs3 = ArrayList<Song>()
        songs3.add(Song("Dynamite", "방탄소년단", "Cos ah ah I'm in the stars tonight\nSo watch me bring the fire",  R.drawable.img_album_dynamite, 199, "dynamite_bts", 1, false, 0, 0))
        val album3 = Album("DYNAMITE (DayTime Ver.)", "방탄소년단", "2020.08.21", "가요 / 댄스", R.drawable.img_album_dynamite, 2, songs3)

        val songs4 = ArrayList<Song>()
        songs4.add(Song("Butter", "방탄소년단", "Smooth like butter\nLike a criminal undercover",  R.drawable.img_album_butter, 144, "butter_bts", 1, false, 0, 0))
        val album4 = Album("Butter", "방탄소년단", "2021.05.21", "가요 / 댄스", R.drawable.img_album_butter, 2, songs4)

        val songs5 = ArrayList<Song>()
        songs5.add(Song("strawberry moon", "아이유(IU)", "달이 익어가니 서둘러 젊은 피야\n민들레 한 송이 들고", R.drawable.img_album_strawberry, 205, "strawberry_moon_iu", 1, false, 0, 0))
        val album5 = Album("strawberry moon", "아이유(IU)", "2021.10.19", "가요 / 락", R.drawable.img_album_strawberry, 2, songs5)

        val songs6 = ArrayList<Song>()
        songs6.add(Song("신호등", "이무진", "이제야 목적지를 정했지만\n가려한 날 막아서네 난 갈 길이 먼데", R.drawable.img_album_traffic_light, 232, "traffic_light_lee_mujin", 0, false, 0, 0))
        val album6 = Album("신호등", "이무진", "2021.05.14", "가요 / 락", R.drawable.img_album_traffic_light, 2, songs6)

        albums.add(album1)
        albums.add(album2)
        albums.add(album3)
        albums.add(album4)
        albums.add(album5)
        albums.add(album6)
    }

    override fun onStart() {
        super.onStart()

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val jsonSongData = sharedPreferences.getString("song", null)
        if(jsonSongData == null) {
            song = albums[0].songs.get(0)
        } else {
            song = gson.fromJson(jsonSongData, Song::class.java)

            for(i in 0 until albums.size) {
                for(j in 0 until albums[i].songs.size)
                    if(song.title.equals(albums[i].songs[j].title)) {
                        albumIndex = i
                        songIndex = j
                        albums[i].songs[j] = song
                        break
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
        song.second = (binding.mainMiniplayerSb.progress * song.length) / 1000

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

    fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerSb.progress = song.second * 1000 / song.length

        mediaPlayer?.release()   // 미디어 플레이어 초기화

        song.isPlaying = false
        miniPlayer = MiniPlayer(song.length, song.isPlaying, song.second)

        val musicFile = resources.getIdentifier(song.filename, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, musicFile)
        miniPlayer.start()
    }

    inner class MiniPlayer(private val length : Int, var isPlaying : Boolean, var savedSecond : Int) : Thread() {
        var second = savedSecond

        override fun run() {
            try {
                print(length)
                while (true) {
                    if (second >= length)
                        break

                    if (isPlaying) {
                        sleep(1000)
                        second++
                        handler.post {
                            binding.mainMiniplayerSb.progress = second * 1000 / length
                        }
                    }
                }
            } catch (e : InterruptedException) {
                Log.d("Interrupt", "Thread가 종료되었습니다.")
            }
        }
    }
}
package com.example.floclone

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.floclone.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding

    private lateinit var player : Player
    private var song : Song = Song()
    private val handler = Handler(Looper.getMainLooper())
    private var mediaPlayer : MediaPlayer? = null    // ?: nullable object
    private var gson : Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()

        binding.songDownIv.setOnClickListener { finish() }

        binding.btnPlayerPlay.setOnClickListener {
            player.isPlaying = true
            song.isPlaying = true
            setPlayerStatus(true)
            mediaPlayer?.start()
            mediaPlayer?.seekTo(song.playPos)
        }

        binding.btnPlayerPause.setOnClickListener { onPause() }

        binding.btnPlayerPrevious.setOnClickListener() {
            player.isPlaying = false
            song.isPlaying = false
            setPlayerStatus(false)   // 정지 상태일때의 이미지로 전환
            player.interrupt()

            binding.songPlayerSb.progress = 0   //Seekbar 초기화
            binding.startTimeTv.text = "00:00"
            song.playPos = 0
            player = Player(song.length, song.isPlaying, song.playPos)
            player.start()
        }

        binding.btnPlayerNext.setOnClickListener() {

        }

        binding.btnPlayerRepeatNo.setOnClickListener { setRepeat(1) }
        binding.btnPlayerRepeat.setOnClickListener { setRepeat(2) }
        binding.btnPlayerRepeat1.setOnClickListener { setRepeat(3) }
        binding.btnPlayerRepeatList.setOnClickListener { setRepeat(0) }

        binding.btnPlayerRandomNo.setOnClickListener { setRandomPlay(true) }
        binding.btnPlayerRandom.setOnClickListener { setRandomPlay(false) }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()   // 미디어플레이어 중지
        player.isPlaying = false // Thread 중지
        song.second = (binding.songPlayerSb.progress * song.length) / 1000
        song.playPos = mediaPlayer?.currentPosition!!
        setPlayerStatus(false)   // 정지 상태일때의 이미지로 전환

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()   // sharedPreference 조작
        //GSON : JSON 생성기
        val json = gson.toJson(song)
        editor.putString("song", json)

        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()
        mediaPlayer?.release()   // 미디어 플레이어가 갖고 있던 리소스 해제 (song)
        mediaPlayer = null       // 미디어 플레이어 해제
    }

    private fun initSong() {
        if(intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("lyrics") && intent.hasExtra("backgroundImageRes") && intent.hasExtra("length") && intent.hasExtra("filename") && intent.hasExtra("trackNum") && intent.hasExtra("isPlaying") && intent.hasExtra("second") && intent.hasExtra("playPos")) {
            val title = intent.getStringExtra("title")!!
            val singer = intent.getStringExtra("singer")!!
            val lyrics = intent.getStringExtra("lyrics")!!
            val backgroundImageRes = intent.getIntExtra("backgroundImageRes", 0)
            val length = intent.getIntExtra("length", 0)
            val filename = intent.getStringExtra("filename")!!
            val trackNum = intent.getIntExtra("trackNum", 0)
            val isPlaying = intent.getBooleanExtra("isPlaying", false)
            val second = intent.getIntExtra("second", 0)
            val playPos = intent.getIntExtra("playPos", 0)

            song = Song(title, singer, lyrics, backgroundImageRes, length, filename, trackNum, isPlaying, second, playPos)

            val musicFile = resources.getIdentifier(song.filename, "raw", this.packageName)

            binding.endTimeTv.text = String.format("%02d:%02d", song.length / 60, song.length % 60)
            binding.songMusicTitleTv.text = song.title
            binding.songSingerNameTv.text = song.singer
            binding.songLyricsTv.text = song.lyrics
            binding.songAlbumImage.setImageResource(song.backgroundImageRes)
            binding.songPlayerSb.progress = song.second * 1000 / song.length
            binding.startTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)

            setPlayerStatus(song.isPlaying)

            mediaPlayer = MediaPlayer.create(this, musicFile)

            player = Player(song.length, song.isPlaying, song.second)
            player.start()
        }
    }

    private fun setPlayerStatus(isPlaying : Boolean) {
        if(!isPlaying) {
            binding.btnPlayerPause.visibility = View.GONE
            binding.btnPlayerPlay.visibility = View.VISIBLE
        }
        else {
            binding.btnPlayerPlay.visibility = View.GONE
            binding.btnPlayerPause.visibility = View.VISIBLE
        }
    }

    private fun setRepeat(repeatMode : Int) {
        when(repeatMode) {
            0 -> {  // no repeat
                binding.btnPlayerRepeatNo.visibility = View.VISIBLE
                binding.btnPlayerRepeat.visibility = View.GONE
                binding.btnPlayerRepeat1.visibility = View.GONE
                binding.btnPlayerRepeatList.visibility = View.GONE

                player.isPlaying = false
            }

            1 -> {  // repeat all
                binding.btnPlayerRepeatNo.visibility = View.GONE
                binding.btnPlayerRepeat.visibility = View.VISIBLE
                binding.btnPlayerRepeat1.visibility = View.GONE
                binding.btnPlayerRepeatList.visibility = View.GONE

                player.isPlaying = false
            }

            2 -> {  // repeat one song
                binding.btnPlayerRepeatNo.visibility = View.GONE
                binding.btnPlayerRepeat.visibility = View.GONE
                binding.btnPlayerRepeat1.visibility = View.VISIBLE
                binding.btnPlayerRepeatList.visibility = View.GONE

                player = Player(song.length, true, song.second)
                player.start()
            }

            3 -> {  // repeat songs in playlist
                binding.btnPlayerRepeat.visibility = View.GONE
                binding.btnPlayerRepeat.visibility = View.GONE
                binding.btnPlayerRepeat1.visibility = View.GONE
                binding.btnPlayerRepeatList.visibility = View.VISIBLE

                player.isPlaying = false
            }
        }
    }

    private fun setRandomPlay(isRandom : Boolean) {
        if(isRandom) {
            binding.btnPlayerRandomNo.visibility = View.GONE
            binding.btnPlayerRandom.visibility = View.VISIBLE
        }
        else {
            binding.btnPlayerRandomNo.visibility = View.VISIBLE
            binding.btnPlayerRandom.visibility = View.GONE
        }
    }

    inner class Player(private val length : Int, var isPlaying : Boolean, private val savedSecond : Int) : Thread() {
        private var second = savedSecond

        override fun run() {
            try {
                while (true) {
                    if (second >= length)
                        break

                    if (isPlaying) {
                        sleep(1000)
                        second++
                        handler.post {
                            binding.songPlayerSb.progress = second * 1000 / length
                            binding.startTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                        }
                    }

                }
            } catch (e : InterruptedException) {
                Log.d("Interrupt", "Thread가 종료되었습니다.")
            }

        }
    }
}
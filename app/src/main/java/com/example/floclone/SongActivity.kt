package com.example.floclone

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.floclone.databinding.ActivityMainBinding
import com.example.floclone.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding

    private lateinit var player : Player
    private var song : Song = Song()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()

        player = Player(song.playTime, song.isPlaying)
        player.start()

        binding.songDownIv.setOnClickListener {
            finish()
        }

        binding.btnPlayerPlay.setOnClickListener {
            player.isPlaying = true
            setPlayerStatus(true)
        }

        binding.btnPlayerPause.setOnClickListener {
            player.isPlaying = false
            setPlayerStatus(false)
        }

        binding.btnPlayerRepeatNo.setOnClickListener {
            setRepeat(1)
        }
        binding.btnPlayerRepeat.setOnClickListener {
            setRepeat(2)
        }
        binding.btnPlayerRepeat1.setOnClickListener {
            setRepeat(3)
        }
        binding.btnPlayerRepeatList.setOnClickListener {
            setRepeat(0)
        }

        binding.btnPlayerRandomNo.setOnClickListener {
            setRandomPlay(true)
        }
        binding.btnPlayerRandom.setOnClickListener {
            setRandomPlay(false)
        }
    }

    private fun initSong() {
        if(intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("lyrics") && intent.hasExtra("backgroundImageRes") && intent.hasExtra("playTime") && intent.hasExtra("isPlaying")) {
            song.title = intent.getStringExtra("title")!!
            song.singer = intent.getStringExtra("singer")!!
            song.lyrics = intent.getStringExtra("lyrics")!!
            song.backgroundImageRes = intent.getIntExtra("backgroundImageRes", 0)
            song.playTime = intent.getIntExtra("playTime", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)

            binding.endTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
            binding.songMusicTitleTv.text = song.title
            binding.songSingerNameTv.text = song.singer
            binding.songLyricsTv.text = song.lyrics
            binding.songAlbumImage.setImageResource(song.backgroundImageRes)
            setPlayerStatus(song.isPlaying)
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

                player = Player(song.playTime, true)
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

    inner class Player(private val playTime : Int, var isPlaying: Boolean) : Thread() {
        private var second = 0

        override fun run() {
            try {
                while (true) {
                    if (second >= playTime)
                        break

                    if (isPlaying) {
                        sleep(1000)
                        second++
                        handler.post {
                            binding.songPlayerSb.progress = second * 1000 / playTime
                            binding.startTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                        }
                    }

                }
            } catch (e : InterruptedException) {
                Log.d("Interrupt", "Thread가 종료되었습니다.")
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()
    }
}
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

    var toggle_random = 0
    var toggle_repeat = 0

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

        binding.btnPlayerRepeat.setOnClickListener {
            if(toggle_repeat != 0) {
                setRepeat(false)
                toggle_repeat = 0
            }
            else {
                setRepeat(true)
                toggle_repeat = 1
            }
        }

        binding.btnPlayerRandom.setOnClickListener {
            if(toggle_random != 0) {
                setRandomPlay(false)
                toggle_random = 0
            }
            else {
                setRandomPlay(true)
                toggle_random = 1
            }
        }
    }

    private fun initSong() {
        if(intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("playTime") && intent.hasExtra("isPlaying")) {
            song.title = intent.getStringExtra("title")!!
            song.singer = intent.getStringExtra("singer")!!
            song.playTime = intent.getIntExtra("playTime", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)

            binding.endTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
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

    private fun setRepeat(isOn : Boolean) {
        if(isOn)
            binding.btnPlayerRepeat.setBackgroundResource(R.drawable.nugu_btn_repeat_inactive)
        else
            binding.btnPlayerRepeat.setBackgroundResource(R.drawable.btn_player_repeat_on_light)
    }

    private fun setRandomPlay(isOn : Boolean) {
        if(isOn)
            binding.btnPlayerRandom.setBackgroundResource(R.drawable.nugu_btn_random_inactive)
        else
            binding.btnPlayerRandom.setBackgroundResource(R.drawable.btn_playlist_random_on)
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
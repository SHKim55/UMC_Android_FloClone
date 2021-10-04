package com.example.floclone

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.floclone.databinding.ActivityMainBinding
import com.example.floclone.databinding.ActivitySongBinding

// ':' - 상속, 코틀린에서는 상속 시 무조건 매개변수 () 사용
class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)    // root: xml 내 최상단 레이아웃

        var toggle_random = 1
        var toggle_repeat = 1

        if(intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }

        binding.songDownIv.setOnClickListener {
            finish()
        }

        binding.btnPlayerPlay.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.btnPlayerPause.setOnClickListener {
            setPlayerStatus(true)
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

    fun setPlayerStatus(isPlaying : Boolean) {
        if(isPlaying) {
            binding.btnPlayerPause.visibility = View.GONE
            binding.btnPlayerPlay.visibility = View.VISIBLE
        }
        else {
            binding.btnPlayerPlay.visibility = View.GONE
            binding.btnPlayerPause.visibility = View.VISIBLE
        }
    }

    fun setRepeat(isOn : Boolean) {
        if(isOn)
            binding.btnPlayerRepeat.setBackgroundResource(R.drawable.nugu_btn_repeat_inactive)
        else
            binding.btnPlayerRepeat.setBackgroundResource(R.drawable.btn_player_repeat_on_light)
    }

    fun setRandomPlay(isOn : Boolean) {
        if(isOn)
            binding.btnPlayerRandom.setBackgroundResource(R.drawable.nugu_btn_random_inactive)
        else
            binding.btnPlayerRandom.setBackgroundResource(R.drawable.btn_playlist_random_on)
    }
}
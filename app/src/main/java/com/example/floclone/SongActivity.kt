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

        if(intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }

        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songMiniPauseIv.setOnClickListener {
            setPlayerStatus(true)
        }

    }

    fun setPlayerStatus(isPlaying : Boolean) {
        if(isPlaying) {
            binding.songPauseIv.visibility = View.GONE
            binding.songMiniplayerIv.visibility = View.VISIBLE
        }
        else {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
    }
}
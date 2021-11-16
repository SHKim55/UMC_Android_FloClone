package com.example.floclone

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.floclone.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    private lateinit var songDB : SongDatabase

    private val handler = Handler(Looper.getMainLooper())
    private var mediaPlayer : MediaPlayer? = null    // ?: nullable object
    private lateinit var player : Player

    private var songs = ArrayList<Song>()
    private var nowPos : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()
        setPlayer(songs[nowPos])
    }

    override fun onPause() {
        super.onPause()

        mediaPlayer?.pause()   // 미디어플레이어 중지
        player.isPlaying = false // Thread 중지
        songs[nowPos].second = (binding.songPlayerSb.progress * songs[nowPos].length) / 1000
        songs[nowPos].playPos = mediaPlayer?.currentPosition!!
        setPlayerStatus(false)   // 정지 상태일때의 이미지로 전환

        songDB.SongDao().update(songs[nowPos])

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()
        mediaPlayer?.release()   // 미디어 플레이어가 갖고 있던 리소스 해제 (song)
        mediaPlayer = null       // 미디어 플레이어 해제
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.SongDao().getSongs())
    }

    private fun initSong() {
        val sharedPreference = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreference.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        val musicFile = resources.getIdentifier(songs[nowPos].filename, "raw", this.packageName)

        setPlayer(songs[nowPos])
        setPlayerStatus(songs[nowPos].isPlaying)

        mediaPlayer = MediaPlayer.create(this, musicFile)

        player = Player(songs[nowPos].length, songs[nowPos].isPlaying, songs[nowPos].second)
        player.start()
    }

    private fun initClickListener() {
        binding.songDownIv.setOnClickListener { finish() }

        binding.btnPlayerPlay.setOnClickListener {
            player.isPlaying = true
            songs[nowPos].isPlaying = true
            setPlayerStatus(true)
            mediaPlayer?.start()
            mediaPlayer?.seekTo(songs[nowPos].playPos)
        }

        binding.btnPlayerPause.setOnClickListener { onPause() }
        binding.btnPlayerPrevious.setOnClickListener { moveSong(-1) }
        binding.btnPlayerNext.setOnClickListener { moveSong(1) }

        binding.btnPlayerRepeatNo.setOnClickListener { setRepeat(1) }
        binding.btnPlayerRepeat.setOnClickListener { setRepeat(2) }
        binding.btnPlayerRepeat1.setOnClickListener { setRepeat(3) }
        binding.btnPlayerRepeatList.setOnClickListener { setRepeat(0) }

        binding.btnPlayerRandomNo.setOnClickListener { setRandomPlay(true) }
        binding.btnPlayerRandom.setOnClickListener { setRandomPlay(false) }

        binding.btnSongLikeIv.setOnClickListener {
            val liked = songs[nowPos].isLike
            songs[nowPos].isLike = !liked
            songDB.SongDao().updateIsLikeById(songs[nowPos].isLike, songs[nowPos].id)

            setLike(songs[nowPos].isLike)
        }
    }

    private fun getPlayingSongPosition(songId : Int) : Int {
        for(i in 0 until songs.size) {
            if(songs[i].id == songId)
                return i
        }
        return 0
    }

    private fun setPlayer(song : Song) {
        binding.endTimeTv.text = String.format("%02d:%02d", song.length / 60, song.length % 60)
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songLyricsTv.text = song.lyrics
        binding.songAlbumImage.setImageResource(song.backgroundImageRes)
        binding.songPlayerSb.progress = song.second * 1000 / song.length
        binding.startTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        setLike(songs[nowPos].isLike)
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

                player = Player(songs[nowPos].length, true, songs[nowPos].second)
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

    private fun setLike(isLike : Boolean) {
        if(isLike) {
            binding.btnSongLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.btnSongLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun moveSong(offset : Int) {
        if(nowPos + offset < 0) {
            Toast.makeText(this, "첫번째 곡입니다.", Toast.LENGTH_SHORT).show()
        } else if(nowPos + offset > songs.size - 1) {
            Toast.makeText(this, "마지막 곡입니다.", Toast.LENGTH_SHORT).show()
        } else {
            songs[nowPos].isPlaying = false
            songDB.SongDao().update(songs[nowPos])

            setPlayerStatus(false)   // 정지 상태일때의 이미지로 전환
            player.interrupt()
            mediaPlayer?.release()   // 미디어 플레이어가 갖고 있던 리소스 해제 (song)
            mediaPlayer = null       // 미디어 플레이어 해제

            nowPos += offset
            setPlayer(songs[nowPos])

            val musicFile = resources.getIdentifier(songs[nowPos].filename, "raw", this.packageName)
            Log.d("This music file : ", songs[nowPos].filename)
            mediaPlayer = MediaPlayer.create(this, musicFile)

            binding.songPlayerSb.progress = 0   //Seekbar 초기화
            binding.startTimeTv.text = "00:00"
            songs[nowPos].playPos = 0
            player = Player(songs[nowPos].length, songs[nowPos].isPlaying, songs[nowPos].playPos)
            player.start()
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
package com.example.floclone

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone.databinding.ItemSavedSongBinding

class SavedSongRVAdapter() : RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()
    interface SavedSongItemClickListener {
//        fun onItemClick(song : Song)
        fun onMoreButtonClick(songId : Int)
    }

    // 리스너 객체 저장 변수
    private lateinit var savedSongItemClickListener: SavedSongItemClickListener

    fun setSavedSongItemClickListener(itemClickListener: SavedSongItemClickListener) {
        savedSongItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SavedSongRVAdapter.ViewHolder {
        val binding : ItemSavedSongBinding = ItemSavedSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])
//        holder.itemView.setOnClickListener() { savedSongItemClickListener.onItemClick(songList[position]) }

        holder.binding.savedSongListMoreIv.setOnClickListener() {
            savedSongItemClickListener.onMoreButtonClick(songs[position].id)
            removeSong(position)
        }
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs : ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)
    }

    fun removeSong(position : Int) {
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSavedSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.savedSongImgIv.setImageResource(song.backgroundImageRes)
            binding.savedSongListTitleTv.text = song.title
            binding.savedSongListSingerTv.text = song.singer
        }
    }
}
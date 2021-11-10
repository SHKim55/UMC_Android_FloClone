package com.example.floclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone.databinding.ItemSongBinding

class SongRVAdapter(private val songList: ArrayList<Song>) : RecyclerView.Adapter<SongRVAdapter.ViewHolder>() {
    //클릭 인터페이스 정의
    interface SongItemClickListener {
        fun onItemClick(song : Song)
        fun onMoreButtonClick(position: Int)
    }

    // 리스너 객체 저장 변수
    private lateinit var songItemClickListener: SongItemClickListener

    fun setMyItemClickListener(itemClickListener: SongItemClickListener) {
        songItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SongRVAdapter.ViewHolder {
        val binding : ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongRVAdapter.ViewHolder, position: Int) {
        holder.bind(songList[position])
        holder.itemView.setOnClickListener() { songItemClickListener.onItemClick(songList[position]) }

        holder.binding.songListMoreIv.setOnClickListener() { songItemClickListener.onMoreButtonClick(position) }
    }

    override fun getItemCount(): Int = songList.size

    fun removeItem(position : Int) {
        songList.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.songListNumberTv.text = "0${song.trackNum}"
            binding.songListTitleTv.text = song.title
            binding.songListSingerTv.text = song.singer

            if(song.isTitle)
                binding.songTitleFlagTv.visibility = View.VISIBLE
            else
                binding.songTitleFlagTv.visibility = View.GONE
        }
    }
}
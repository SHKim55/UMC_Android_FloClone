package com.example.floclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone.databinding.ItemSavedSongBinding
import com.example.floclone.databinding.ItemSongBinding

class SavedSongRVAdapter(private val songList: ArrayList<Song>) : RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {
    //클릭 인터페이스 정의
    interface SavedSongItemClickListener {
//        fun onItemClick(song : Song)
        fun onMoreButtonClick(position: Int)
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
        holder.bind(songList[position])
//        holder.itemView.setOnClickListener() { savedSongItemClickListener.onItemClick(songList[position]) }

        holder.binding.savedSongListMoreIv.setOnClickListener() { savedSongItemClickListener.onMoreButtonClick(position) }
    }

    override fun getItemCount(): Int = songList.size

    fun removeItem(position : Int) {
        songList.removeAt(position)
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
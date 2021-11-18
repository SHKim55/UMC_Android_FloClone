package com.example.floclone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import com.example.floclone.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: ArrayList<Album>, private val songDB : SongDatabase)
    : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {
    //클릭 인터페이스 정의
    interface AlbumItemClickListener {
        fun onItemClick(album : Album)
        fun onPlayButtonClick(id : Int)
    }

    // 리스너 객체 저장 변수
    private lateinit var albumItemClickListener: AlbumItemClickListener

    fun setAlbumItemClickListener(itemClickListener: AlbumItemClickListener) {
        albumItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding : ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        // position = id - 1
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener() { albumItemClickListener.onItemClick(albumList[position]) }
        holder.binding.albumImgPlayIv.setOnClickListener() { albumItemClickListener.onPlayButtonClick(position + 1) }
    }

    override fun getItemCount(): Int = albumList.size

    fun addItem(album : Album) {
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position : Int) {
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    //ViewHolder
    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.albumImgIv.setImageResource(album.backgroundImageRes)
            binding.albumSongTitleTv.text = songDB.SongDao().getSongsInAlbum(album.id).get(0).title
            binding.albumSingerTv.text = album.singer
        }
    }
}
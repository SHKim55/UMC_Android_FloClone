package com.example.floclone

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone.databinding.ItemSavedAlbumBinding

class SavedAlbumRVAdapter(userId : Int) : RecyclerView.Adapter<SavedAlbumRVAdapter.ViewHolder>() {
    private val albums = ArrayList<Album>()
    private val id = userId
    interface SavedAlbumItemClickListener {
        fun onMoreButtonClick(userId : Int, albumId : Int)
    }

    private lateinit var savedAlbumItemClickListener : SavedAlbumItemClickListener

    fun setSavedAlbumItemClickListener(itemClickListener: SavedAlbumRVAdapter.SavedAlbumItemClickListener) {
        savedAlbumItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SavedAlbumRVAdapter.ViewHolder {
        val binding : ItemSavedAlbumBinding = ItemSavedAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedAlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albums[position])

        holder.binding.savedAlbumListMoreIv.setOnClickListener {
            savedAlbumItemClickListener.onMoreButtonClick(id, albums[position].id)
            removeAlbum(position)
        }
    }

    override fun getItemCount(): Int = albums.size

    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums : ArrayList<Album>) {
        this.albums.clear()
        this.albums.addAll(albums)
    }

    fun removeAlbum(position : Int) {
        albums.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSavedAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.savedAlbumImgIv.setImageResource(album.backgroundImageRes)
            binding.savedAlbumListTitleTv.text = album.title
            binding.savedAlbumListSingerTv.text = album.singer

            var scaleText = ""
            when(album.albumScale) {
                0 -> { scaleText = "정규" }
                1 -> { scaleText = "미니" }
                2 -> { scaleText = "싱글" }
            }
            binding.savedAlbumListDescriptionTv.text = album.releaseDate + " | " + scaleText + " | " + album.genre
        }
    }

}
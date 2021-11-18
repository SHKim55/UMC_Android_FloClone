package com.example.floclone

import androidx.room.*

@Dao
interface SongDao {
    @Insert
    fun insert(song : Song)

    @Update
    fun update(song : Song)

    @Delete
    fun delete(song : Song)

    @Query("SELECT * FROM SongTable")
    fun getSongs() : List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id : Int) : Song

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getLikedSongs(isLike: Boolean) : List<Song>

    @Query("SELECT * FROM SongTable WHERE albumIdx = :albumIdx")
    fun getSongsInAlbum(albumIdx: Int): List<Song>

    @Query("UPDATE SongTable SET second = :second WHERE id = :id")
    fun updateSecondById(second : Int, id : Int)

    @Query("UPDATE SongTable SET isLike = :isLike WHERE id = :id")
    fun updateIsLikeById(isLike : Boolean, id : Int)

    @Query("DELETE FROM SongTable")
    fun deleteAll()

}
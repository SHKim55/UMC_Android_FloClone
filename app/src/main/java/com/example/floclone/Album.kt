package com.example.floclone

data class Album (
    val title : String = "",
    val singer : String = "",
    val releaseDate : String = "",
    val genre : String = "",
    val backgroundImageRes : Int = 0,
    val albumScale : Int = 0,    // 0: Original, 1: Mini, 2: Single
    val songs : ArrayList<Song>
)

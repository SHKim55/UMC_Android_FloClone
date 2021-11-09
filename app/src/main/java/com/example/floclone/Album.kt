package com.example.floclone

data class Album (
    var title : String = "",
    var singer : String = "",
    var releaseDate : String = "",
    var genre : String = "",
    var backgroundImageRes : Int = 0,
    var albumScale : Int = 0,    // 0: Original, 1: Mini, 2: Single
)

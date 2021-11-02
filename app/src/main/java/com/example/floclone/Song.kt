package com.example.floclone

data class Song(
    var title : String = "",
    var singer : String = "",
    var lyrics : String = "",
    var backgroundImageRes : Int = 0,
    var playTime : Int = 0,
    var isPlaying : Boolean = false,
    var second : Int = 0,
    var music : String = "",
    var playPos : Int = 0,
)

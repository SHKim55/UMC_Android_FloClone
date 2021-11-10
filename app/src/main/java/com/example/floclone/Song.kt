package com.example.floclone

data class Song(
    val title : String = "",
    val singer : String = "",
    val lyrics : String = "",
    val backgroundImageRes : Int = 0,
    val length : Int = 0,
    val filename : String = "",
    val trackNum : Int = 0,
    var isPlaying : Boolean = false,
    var second : Int = 0,
    var playPos : Int = 0,
    val isTitle : Boolean = false,
)

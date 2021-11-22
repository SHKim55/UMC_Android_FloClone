package com.example.floclone

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LikeTable")
data class Like(
    val userId : Int,
    val albumId : Int,

    @PrimaryKey(autoGenerate = true)
    val id : Int = 0
)

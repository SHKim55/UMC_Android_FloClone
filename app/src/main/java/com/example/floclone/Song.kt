package com.example.floclone

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(tableName = "SongTable")
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
    var isLike : Boolean = false,
    var albumIdx : Int = 0,    // 어느 Album에 속해 있는지 저장하는 변수

    @PrimaryKey(autoGenerate = false)
    var id : Int = 0
)

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    // From version 1 to version 2
    override fun migrate(database: SupportSQLiteDatabase) {

    }
}
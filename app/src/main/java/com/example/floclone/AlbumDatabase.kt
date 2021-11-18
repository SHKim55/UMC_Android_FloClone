package com.example.floclone

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Album::class], version = 1)
abstract class AlbumDatabase : RoomDatabase() {
    abstract fun AlbumDao() : AlbumDao

    companion object {
        private var instance: AlbumDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AlbumDatabase? {
            if (instance == null) {
                synchronized(AlbumDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext, AlbumDatabase::class.java, "user-database"
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return instance
        }
    }
}
package com.example.floclone

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

import androidx.room.migration.Migration

@Database(entities = [Song::class, User::class], version = 2)   //AlbumDB와 동기화를 위해 Schema 수정
abstract class SongDatabase : RoomDatabase() {
    abstract fun SongDao() : SongDao
    abstract fun UserDao() : UserDao

    companion object {
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if (instance == null) {
                synchronized(SongDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext, SongDatabase::class.java, "user-database"
                    ).allowMainThreadQueries().addMigrations(MIGRATION_1_2).build()
                }
            }
            return instance
        }
    }
}
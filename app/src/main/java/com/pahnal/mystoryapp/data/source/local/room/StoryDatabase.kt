package com.pahnal.mystoryapp.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pahnal.mystoryapp.data.source.local.dao.RemoteKeysDao
import com.pahnal.mystoryapp.data.source.local.dao.StoryDao
import com.pahnal.mystoryapp.domain.model.RemoteKeys
import com.pahnal.mystoryapp.domain.model.Story

@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 1,
    exportSchema = false,
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, "quote_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
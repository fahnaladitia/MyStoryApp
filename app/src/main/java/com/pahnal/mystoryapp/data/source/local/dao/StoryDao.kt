package com.pahnal.mystoryapp.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pahnal.mystoryapp.domain.model.Story

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<Story>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, Story>

    @Query("SELECT * FROM story")
    fun getAllStories(): LiveData<List<Story>>

    @Query("SELECT * FROM story WHERE id=:id")
    fun getStoryById(id: String): LiveData<Story>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}
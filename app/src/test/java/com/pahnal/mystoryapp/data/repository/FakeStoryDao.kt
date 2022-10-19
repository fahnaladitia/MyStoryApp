package com.pahnal.mystoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pahnal.mystoryapp.data.source.local.dao.StoryDao
import com.pahnal.mystoryapp.domain.model.Story

class FakeStoryDao : StoryDao {

    private val memory = ArrayList<Story>()

    override suspend fun insertStory(stories: List<Story>) {
        memory.addAll(stories)
    }

    override fun getAllStory(): PagingSource<Int, Story> {
        return object : PagingSource<Int, Story>() {
            override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
                return state.anchorPosition?.let { 1 }
            }

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
                return LoadResult.Page(
                    data = memory,
                    prevKey = null,
                    nextKey = null,
                )
            }
        }
    }

    override fun getAllStories(): LiveData<List<Story>> {
        val data = MutableLiveData<List<Story>>()
        data.value = memory
        return data
    }

    override fun getStoryById(id: String): LiveData<Story> {
        val data = MutableLiveData<Story>()
        data.value = memory.find { it.id == id }
        return data
    }

    override suspend fun deleteAll() {
        memory.clear()
    }
}
package com.pahnal.mystoryapp.data.paginator

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pahnal.mystoryapp.data.mapper.toDomainStoryList
import com.pahnal.mystoryapp.data.source.remote.network.ApiService
import com.pahnal.mystoryapp.domain.model.Story

class StoryPagingSource(private val apiService: ApiService, private val token: String?) :
    PagingSource<Int, Story>() {

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { 1 }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX

            val responseData = apiService.getAllStories(
                "Bearer ${token ?: ""}", 1, position, params.loadSize,
            ).toDomainStoryList()

            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
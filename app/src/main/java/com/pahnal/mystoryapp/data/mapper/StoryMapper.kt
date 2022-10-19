package com.pahnal.mystoryapp.data.mapper

import com.pahnal.mystoryapp.data.source.remote.dto.response.GetAllStoriesResponseDto
import com.pahnal.mystoryapp.domain.model.Story

fun GetAllStoriesResponseDto.toDomainStoryList(): List<Story> {
    return this.listStory?.map {
        Story(
            createdAt = it?.createdAt ?: "",
            description = it?.description ?: "",
            id = it?.id ?: "",
            name = it?.name ?: "",
            photoUrl = it?.photoUrl ?: "",
            lat = it?.lat,
            lon = it?.lon,
        )
    } ?: emptyList()
}
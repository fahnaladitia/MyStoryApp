package com.pahnal.mystoryapp.utils

import com.pahnal.mystoryapp.data.source.remote.dto.response.BasicResponseDto
import com.pahnal.mystoryapp.data.source.remote.dto.response.GetAllStoriesResponseDto
import com.pahnal.mystoryapp.data.source.remote.dto.response.LoginResponseDto
import com.pahnal.mystoryapp.domain.model.Story

object DataDummy {

    fun generateRegisterSuccess(): BasicResponseDto {
        return BasicResponseDto(
            error = false,
            message = "User Created",
        )
    }

    fun generateRegisterErrorInvalidEmail(): BasicResponseDto {
        return BasicResponseDto(
            error = true,
            message = "\"email\" must be a valid email",
        )
    }
    fun generateRegisterErrorEmailAlreadyTaken(): BasicResponseDto {
        return BasicResponseDto(
            error = true,
            message = "Email is already taken",
        )
    }

    fun generateDummyLoginSuccess(): LoginResponseDto {
        return LoginResponseDto(
            error = false,
            message = "success",
            loginResult = LoginResponseDto.LoginResultResponseDto(
                name = "Arczel",
                token = "Token",
                userId = "1",
            )
        )
    }

    fun generateDummyLoginErrorInvalidPassword(): BasicResponseDto {
        return BasicResponseDto(
            error = true,
            message = "Invalid password",
        )
    }

    fun generateDummyLoginErrorUserNotFound(): BasicResponseDto {
        return BasicResponseDto(
            error = true,
            message = "User not found",
        )
    }

    fun generateDummyLoginErrorInvalidEmail(): BasicResponseDto {
        return BasicResponseDto(
            error = true,
            message = "\"email\" must be a valid email",
        )
    }

    fun generateAddStorySuccess(): BasicResponseDto {
        return BasicResponseDto(
            error = false,
            message = "success",
        )
    }

    fun generateAddStoryError(): BasicResponseDto {
        return BasicResponseDto(
            error = true,
            message = "Error Add Story",
        )
    }

    fun generateDummyStoriesResponse(): GetAllStoriesResponseDto {
        return GetAllStoriesResponseDto(
            error = false,
            message = "Stories fetched successfully",
            listStory = generateDummyStories().map {
                GetAllStoriesResponseDto.StoryGetAllStoriesResponseDto(
                    createdAt = it.createdAt,
                    description = it.description,
                    id = it.id,
                    lat = it.lat,
                    lon = it.lon,
                    name = it.name,
                    photoUrl = it.photoUrl,
                )
            },
        )
    }

    fun generateDummyStories(): List<Story> {
        val items: ArrayList<Story> = arrayListOf()
        for (i in 0..15) {
            val story = Story(
                id = i.toString(),
                createdAt = "2022-10-10T10:00:00Z",
                description = "Story Description $i",
                lat = -0.488646,
                lon = 117.103478,
                name = "Story-$i",
                photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
            )
            items.add(story)
        }
        return items
    }
}
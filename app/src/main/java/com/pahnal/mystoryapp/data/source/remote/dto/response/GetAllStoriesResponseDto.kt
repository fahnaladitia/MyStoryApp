package com.pahnal.mystoryapp.data.source.remote.dto.response


import com.google.gson.annotations.SerializedName

data class GetAllStoriesResponseDto(
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("listStory")
    val listStory: List<StoryGetAllStoriesResponseDto?>?,
    @SerializedName("message")
    val message: String?
) {
    data class StoryGetAllStoriesResponseDto(
        @SerializedName("createdAt")
        val createdAt: String?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("lat")
        val lat: Double?,
        @SerializedName("lon")
        val lon: Double?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("photoUrl")
        val photoUrl: String?
    )
}
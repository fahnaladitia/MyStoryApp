package com.pahnal.mystoryapp.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class BasicResponseDto(
    @SerializedName("error")
    var error: Boolean?,
    @SerializedName("message")
    var message: String?
)

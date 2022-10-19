package com.pahnal.mystoryapp.data.source.remote.dto.response


import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("loginResult")
    val loginResult: LoginResultResponseDto?,
    @SerializedName("message")
    val message: String?
) {
    data class LoginResultResponseDto(
        @SerializedName("name")
        val name: String?,
        @SerializedName("token")
        val token: String?,
        @SerializedName("userId")
        val userId: String?
    )
}
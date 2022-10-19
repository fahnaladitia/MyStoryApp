package com.pahnal.mystoryapp.domain.model

data class AddStory(
    val description: String,
    val lat: Double? = null,
    val lon: Double? = null,
)

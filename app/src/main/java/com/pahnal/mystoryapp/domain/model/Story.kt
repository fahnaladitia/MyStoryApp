package com.pahnal.mystoryapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng


@Entity
data class Story(
    @PrimaryKey
    val id: String,
    val createdAt: String,
    val description: String,
    val name: String,
    val photoUrl: String,
    val lat: Double?,
    val lon: Double?,
) {

    fun latLng(): LatLng? {
        return if (lat != null && lon != null) LatLng(lat, lon) else null
    }
}


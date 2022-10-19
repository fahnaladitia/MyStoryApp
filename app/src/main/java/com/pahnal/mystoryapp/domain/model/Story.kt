package com.pahnal.mystoryapp.domain.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val id: String,
    val createdAt: String,
    val description: String,
    val name: String,
    val photoUrl: String,
    val lat: Double?,
    val lon: Double?,
) : Parcelable {

    fun latLng(): LatLng? {
        return if (lat != null && lon != null) LatLng(lat, lon) else null
    }
}


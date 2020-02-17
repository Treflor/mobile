package com.treflor.models

import androidx.room.Entity
import com.google.android.libraries.maps.model.LatLng
import com.google.gson.Gson

@Entity(tableName = "landmarks")
data class Landmark(
    val id: String,
    val title: String,
    val snippet: String,
    val type: String,
    val images: List<String>,
    val lat: Double,
    val lng: Double
) {
    fun toJson() = Gson().toJson(this)
    fun toLatLng(): LatLng = LatLng(lat, lng)
}
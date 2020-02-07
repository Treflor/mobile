package com.treflor.models

import androidx.room.Entity
import com.google.android.libraries.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@Entity(tableName = "landmarks")
data class Landmark(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("snippet")
    val snippet: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("images")
    val images: List<String>?,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
) {
    fun toJson(): String? = Gson().toJson(this)
    fun toLatLng(): LatLng = LatLng(lat, lng)
}
package com.treflor.models

import androidx.room.ColumnInfo
import com.google.android.libraries.places.api.model.Place

data class TreflorPlace(
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double
) {
    constructor(place: Place?) : this(
        place?.id ?: "",
        place?.name ?: "",
        place?.address ?: "",
        place?.latLng?.latitude ?: 0.0,
        place?.latLng?.longitude ?: 0.0
    )

    fun toStringLatLang(): String = "${latitude},${longitude}"
}
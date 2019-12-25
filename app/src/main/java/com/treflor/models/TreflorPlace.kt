package com.treflor.models

import com.google.android.libraries.places.api.model.Place

data class TreflorPlace(
    val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
) {
    constructor(place: Place?) : this(
        place?.id ?: "",
        place?.name ?: "",
        place?.address ?: "",
        place?.latLng?.latitude ?: 0.0,
        place?.latLng?.longitude ?: 0.0

    )
}
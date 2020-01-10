package com.treflor.models.directionapi


import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class Bounds(
    @SerializedName("northeast")
    @Embedded(prefix = "northeast_")
    val northeast: Location,
    @SerializedName("southwest")
    @Embedded(prefix = "southeast_")
    val southwest: Location
)
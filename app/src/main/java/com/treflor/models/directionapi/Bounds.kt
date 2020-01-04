package com.treflor.models.directionapi


import com.google.gson.annotations.SerializedName

data class Bounds(
    @SerializedName("northeast")
    val northeast: Location,
    @SerializedName("southwest")
    val southwest: Location
)
package com.treflor.data.remote.response


import com.google.gson.annotations.SerializedName
import com.treflor.models.directionapi.Route

data class DirectionApiResponse(
    @SerializedName("routes")
    val routes: List<Route>,
    @SerializedName("status")
    val status: String
)
package com.treflor.models.directionapi


import com.google.gson.annotations.SerializedName
import com.treflor.models.directionapi.Bounds
import com.treflor.models.directionapi.OverviewPolyline

data class Route(
    @SerializedName("bounds")
    val bounds: Bounds,
    @SerializedName("overview_polyline")
    val overviewPolyline: OverviewPolyline
)
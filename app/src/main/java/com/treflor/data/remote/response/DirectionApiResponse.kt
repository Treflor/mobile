package com.treflor.data.remote.response


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.libraries.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.google.maps.android.PolyUtil
import com.treflor.models.directionapi.*

const val CURRENT_DIRECTION_PK = 0

@Entity(tableName = "direction")
data class DirectionApiResponse(
    @SerializedName("bounds")
    @Embedded(prefix = "bound_")
    val bounds: Bounds,
    @SerializedName("distance")
    @Embedded(prefix = "distance_")
    val distance: Distance,
    @SerializedName("duration")
    @Embedded(prefix = "duration_")
    val duration: Duration,
    @SerializedName("end_address")
    val endAddress: String,
    @SerializedName("end_location")
    @Embedded(prefix = "end_location_")
    val endLocation: Location,
    @SerializedName("points")
    val points: String,
    @SerializedName("start_address")
    val startAddress: String,
    @SerializedName("start_location")
    @Embedded(prefix = "start_location_")
    val startLocation: Location,
    @SerializedName("status")
    val status: String
) {
    @PrimaryKey(autoGenerate = false)
    var pk: Int = CURRENT_DIRECTION_PK

    fun decodedPoints(): List<LatLng> = PolyUtil.decode(points)
}
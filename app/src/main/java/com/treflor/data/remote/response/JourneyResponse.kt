package com.treflor.data.remote.response

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.treflor.models.Journey
import com.treflor.models.User

@Entity(tableName = "Journey_responses")
data class JourneyResponse(
    @PrimaryKey
    @SerializedName("_id")
    val id: String,
    @Embedded(prefix = "_user_")
    @SerializedName("user")
    val user: User,
    @Embedded(prefix = "direction_")
    @SerializedName("direction")
    val direction: DirectionApiResponse?,
    @Embedded(prefix = "journey_")
    @SerializedName("journey")
    val journey: Journey?,
    @SerializedName("tracked_locations")
    val trackedLocations: String
)
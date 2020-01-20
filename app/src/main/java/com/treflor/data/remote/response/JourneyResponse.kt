package com.treflor.data.remote.response

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.treflor.models.Journey
import com.treflor.models.User

@Entity(tableName = "Journey_responses")
data class JourneyResponse(
    @PrimaryKey
    @SerializedName("_id")
    val id: String,
    @Embedded(prefix = "user_")
    @SerializedName("user")
    val user: User,
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded(prefix = "direction_")
    @SerializedName("direction")
    val direction: DirectionApiResponse?,
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded(prefix = "journey_")
    @SerializedName("journey")
    val journey: Journey?,
    @SerializedName("tracked_locations")
    val trackedLocations: String
)
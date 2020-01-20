package com.treflor.data.db.entities.journey

import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.treflor.models.Journey

class JourneyListEntity(
    @PrimaryKey
    @SerializedName("_id")
    val id: String,
    @Embedded(prefix = "journey_")
    @SerializedName("journey")
    val journey: Journey?
)
package com.treflor.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_JOURNEY_PK = 0

@Entity(tableName = "journey")
data class Journey(
    val title: String,
    val content: String,
    @Embedded(prefix = "start_location_")
    val startLocation: TreflorPlace,
    @Embedded(prefix = "destination_")
    val destination: TreflorPlace
) {
    @PrimaryKey(autoGenerate = false)
    var pk: Int = CURRENT_JOURNEY_PK
}
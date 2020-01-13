package com.treflor.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_JOURNEY_PK = 0

@Entity(tableName = "journey")
data class Journey(
    val id: String,
    val title: String,
    val content: String,
    @Embedded(prefix = "origin_")
    val origin: TreflorPlace,
    @Embedded(prefix = "destination_")
    val destination: TreflorPlace,
    val level: String,
    val labels: List<String>
) {
    @PrimaryKey(autoGenerate = false)
    var pk: Int = CURRENT_JOURNEY_PK
}
package com.treflor.models

import androidx.room.Entity
import com.google.gson.Gson

@Entity(tableName = "landmarks")
data class Landmark(
    val id: String,
    val title: String,
    val snippet: String,
    val type: String,
    val images: List<String>
) {
    fun toJson() = Gson().toJson(this)
}
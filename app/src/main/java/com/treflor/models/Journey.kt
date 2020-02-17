package com.treflor.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
data class Journey(
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "content")
    val content: String,
    @Embedded(prefix = "origin_")
    val origin: TreflorPlace,
    @Embedded(prefix = "destination_")
    val destination: TreflorPlace,
    @ColumnInfo(name = "level")
    val level: String,
    @ColumnInfo(name = "labels")
    val labels: List<String>,
    @ColumnInfo(name = "image")
    val image: String?
)
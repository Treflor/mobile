package com.treflor.models


import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("birthday")
    val birthday: Long,
    @SerializedName("email")
    val email: String,
    @SerializedName("family_name")
    val familyName: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("given_name")
    val givenName: String,
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: String,
    @SerializedName("photo")
    val photo: String
)
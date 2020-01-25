package com.treflor.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.RawValue

data class User(
    @SerializedName("_id")
    val id: String,
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
    @SerializedName("googleId")
    val googleId: String?,
    @SerializedName("photo")
    val photo: String
)
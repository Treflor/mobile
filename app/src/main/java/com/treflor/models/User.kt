package com.treflor.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_USER_PK = 0

@Entity(tableName = "user")
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
    @SerializedName("id")
    val id: String?,
    @SerializedName("photo")
    val photo: String
) {
    @PrimaryKey(autoGenerate = false)
    var pk: Int = CURRENT_USER_PK
}
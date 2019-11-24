package com.treflor.data.remote.requests

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName(value = "email")
    var email: String = "",
    @SerializedName(value = "password")
    var password: String = "",
    @SerializedName(value = "password2")
    var password2: String = "",
    @SerializedName(value = "family_name")
    var lastName: String = "",
    @SerializedName(value = "given_name")
    var firstName: String = "",
    @SerializedName(value = "gender")
    var gender: String = "male",
    @SerializedName(value = "birthday")
    var birthday: Int = 0,
    @SerializedName(value = "photo")
    var photo: String?
)
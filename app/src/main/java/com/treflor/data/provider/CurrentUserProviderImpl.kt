package com.treflor.data.provider

import android.content.Context
import com.treflor.models.User

const val USER_ID = "user_id"
const val USER_BIRTHDAY = "user_birthday"
const val USER_EMAIL = "user_email"
const val USER_FAMILY_NAME = "user_family_name"
const val USER_GENDER = "user_gender"
const val USER_GIVEN_NAME = "user_given_name"
const val USER_GOOGLE_ID = "user_google_id"
const val USER_PHOTO = "user_photo"

class CurrentUserProviderImpl(context: Context) : PreferenceProvider(context), CurrentUserProvider {
    override fun getCurrentUser(): User? {
        val id = preferences.getString(USER_ID, "")
        val birthday = preferences.getLong(USER_BIRTHDAY, 0)
        val email = preferences.getString(USER_EMAIL, "")
        val familyName = preferences.getString(USER_FAMILY_NAME, "")
        val gender = preferences.getString(USER_GENDER, "")
        val givenName = preferences.getString(USER_GIVEN_NAME, "")
        val googleId = preferences.getString(USER_GOOGLE_ID, "")
        val photo = preferences.getString(USER_PHOTO, "")
        return if (!id.isNullOrEmpty()) User(
            id,
            birthday,
            email!!,
            familyName!!,
            gender!!,
            givenName!!,
            googleId,
            photo!!
        ) else null
    }

    override fun persistCurrentUser(user: User) {
        val editor = preferences.edit()
        editor.putString(USER_ID, user.id)
        editor.putLong(USER_BIRTHDAY, user.birthday)
        editor.putString(USER_EMAIL, user.email)
        editor.putString(USER_FAMILY_NAME, user.familyName)
        editor.putString(USER_GENDER, user.gender)
        editor.putString(USER_GIVEN_NAME, user.givenName)
        editor.putString(USER_GOOGLE_ID, user.googleId)
        editor.putString(USER_PHOTO, user.photo)
        editor.apply()
    }

    override fun deleteCurrentUser() {
        val editor = preferences.edit()
        editor.remove(USER_ID)
        editor.remove(USER_BIRTHDAY)
        editor.remove(USER_EMAIL)
        editor.remove(USER_FAMILY_NAME)
        editor.remove(USER_GENDER)
        editor.remove(USER_GIVEN_NAME)
        editor.remove(USER_GOOGLE_ID)
        editor.remove(USER_PHOTO)
        editor.apply()
    }
}
//filter low pass high pass
//transister
//logic gate
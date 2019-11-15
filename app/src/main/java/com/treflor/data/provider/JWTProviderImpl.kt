package com.treflor.data.provider

import android.content.Context

const val JWT_TOKEN = "JWT_TOKEN"

class JWTProviderImpl(context: Context) : PreferenceProvider(context), JWTProvider {
    override fun getJWT(): String? {
        return preferences.getString(JWT_TOKEN, "")!!
    }

    override fun setJWT(token: String): Boolean {
        return preferences.edit().putString(JWT_TOKEN, token).commit()
    }

    override fun unsetJWT(): Boolean {
        return preferences.edit().remove(JWT_TOKEN).commit()
    }
}
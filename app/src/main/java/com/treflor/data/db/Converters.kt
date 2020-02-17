package com.treflor.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromString(value: String?): List<String>? {

        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    @JvmStatic
    fun fromList(list: List<String>?): String? {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().toJson(list, type)
    }
}
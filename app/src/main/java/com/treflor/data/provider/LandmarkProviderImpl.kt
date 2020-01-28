package com.treflor.data.provider

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.treflor.models.Landmark

const val LANDMARK = "landmark__"
const val LANDMARK_ID = "landmark_id"

class LandmarkProviderImpl(context: Context) : PreferenceProvider(context), LandmarkProvider {
    override val landmarks: LiveData<List<Landmark>>
        get() = _landmarks
    private val _landmarks by lazy { MutableLiveData<List<Landmark>>(getCurrentLandmarks()) }


    override fun getCurrentLandmarks(): List<Landmark> {
        val landmarks = mutableListOf<Landmark>()
        for (i in 0 until getLastId()) {
            landmarks.add(
                Gson().fromJson<Landmark>(
                    preferences.getString(LANDMARK + i, ""),
                    Landmark::class.java
                )
            )
        }
        return landmarks
    }

    override fun persistCurrentLandmark(landmark: Landmark) {
        preferences.edit().putString(LANDMARK + getLastId(), landmark.toJson()).apply()
        increaseLastId()
    }

    override fun deleteLandmarks() {
        for (i in 0 until getLastId()) {
            preferences.edit().remove(LANDMARK + i).apply()
        }
        preferences.edit().remove(LANDMARK_ID).apply()
    }

    private fun getLastId(): Int = preferences.getInt(LANDMARK_ID, 0)
    private fun increaseLastId() = preferences.edit().putInt(LANDMARK_ID, getLastId() + 1).apply()
}
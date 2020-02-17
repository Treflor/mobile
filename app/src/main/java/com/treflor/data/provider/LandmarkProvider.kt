package com.treflor.data.provider

import androidx.lifecycle.LiveData
import com.treflor.models.Landmark

interface LandmarkProvider {
    val landmarks: LiveData<List<Landmark>>
    fun getCurrentLandmarks(): List<Landmark>
    fun persistCurrentLandmark(landmark: Landmark)
    fun deleteLandmarks()
}
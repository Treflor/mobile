package com.treflor.data.remote.datasources

import androidx.lifecycle.LiveData
import com.treflor.models.directionapi.DirectionApiResponse

interface TreflorGoogleServicesNetworkDataSource {
    val direction: LiveData<DirectionApiResponse>
    suspend fun fetchDirection(origin: String, destination: String, mode: String)
}
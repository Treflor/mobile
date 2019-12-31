package com.treflor.data.remote.datasources

import androidx.lifecycle.LiveData
import com.treflor.data.remote.response.DirectionApiResponse

interface GoogleDirectionNetworkDataSource {
    val direction: LiveData<DirectionApiResponse>
    suspend fun fetchDirection(origin: String, destination: String, mode: String)
}
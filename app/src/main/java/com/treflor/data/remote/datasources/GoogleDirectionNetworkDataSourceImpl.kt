package com.treflor.data.remote.datasources

import android.util.Log
import com.treflor.data.remote.api.GoogleDirectionApiService
import com.treflor.data.remote.response.DirectionApiResponse
import com.treflor.internal.NoConnectivityException

class GoogleDirectionNetworkDataSourceImpl(
    private val googleDirectionApiService: GoogleDirectionApiService
) : GoogleDirectionNetworkDataSource {
    override suspend fun getDirection(
        origin: String,
        destination: String,
        mode: String
    ): DirectionApiResponse? {
        return try {
            googleDirectionApiService.getDirection(origin, destination, mode).await()
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            null
        }
    }
}
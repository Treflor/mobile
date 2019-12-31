package com.treflor.data.remote.datasources

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.data.remote.api.GoogleDirectionApiService
import com.treflor.data.remote.response.DirectionApiResponse
import com.treflor.internal.NoConnectivityException

class GoogleDirectionNetworkDataSourceImpl(
    private val googleDirectionApiService: GoogleDirectionApiService
) : GoogleDirectionNetworkDataSource {


    override val direction: LiveData<DirectionApiResponse> get() = _direction
    private val _direction by lazy { MutableLiveData<DirectionApiResponse>() }


    override suspend fun fetchDirection(
        origin: String,
        destination: String,
        mode: String
    ) {
        try {
            _direction.postValue(
                googleDirectionApiService.fetchDirection(
                    origin,
                    destination,
                    mode
                ).await()
            )
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)

        }
    }
}
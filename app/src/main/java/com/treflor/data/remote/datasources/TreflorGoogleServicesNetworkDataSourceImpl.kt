package com.treflor.data.remote.datasources

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.data.provider.JWTProvider
import com.treflor.data.remote.api.TreflorGoogleApiService
import com.treflor.data.remote.response.DirectionApiResponse
import com.treflor.internal.NoConnectivityException

class TreflorGoogleServicesNetworkDataSourceImpl(
    private val treflorGoogleApiService: TreflorGoogleApiService,
    private val jwtProvider: JWTProvider
) : TreflorGoogleServicesNetworkDataSource {


    override val direction: LiveData<DirectionApiResponse> get() = _direction
    private val _direction by lazy { MutableLiveData<DirectionApiResponse>() }


    override suspend fun fetchDirection(
        origin: String,
        destination: String,
        mode: String
    ) {
        try {
            _direction.postValue(
                treflorGoogleApiService.fetchDirection(
                    jwtProvider.getJWT()!!,
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
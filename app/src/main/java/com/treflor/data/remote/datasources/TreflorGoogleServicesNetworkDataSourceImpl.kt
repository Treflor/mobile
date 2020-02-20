package com.treflor.data.remote.datasources

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.data.provider.JWTProvider
import com.treflor.data.remote.api.TreflorApiService
import com.treflor.models.directionapi.DirectionApiResponse
import com.treflor.internal.NoConnectivityException

class TreflorGoogleServicesNetworkDataSourceImpl(
    private val treflorApiService: TreflorApiService,
    private val jwtProvider: JWTProvider
) : TreflorGoogleServicesNetworkDataSource {


    override val direction: LiveData<DirectionApiResponse> get() = _direction
    private val _direction by lazy { MutableLiveData<DirectionApiResponse>() }

    override suspend fun fetchDirection(
        origin: String,
        destination: String,
        mode: String
    ) {
        // TODO:handle 401
        try {
            _direction.postValue(
                treflorApiService.fetchDirection(
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
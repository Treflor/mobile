package com.treflor.data.remote.datasources

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.data.provider.JWTProvider
import com.treflor.data.remote.api.TreflorUserApiService
import com.treflor.internal.NoConnectivityException
import com.treflor.internal.UnauthorizedException
import com.treflor.models.User
import retrofit2.HttpException

class UserNetworkDataSourceImpl(
    private val treflorUserApiService: TreflorUserApiService,
    private val jwtProvider: JWTProvider
) : UserNetworkDataSource {

    override val user: LiveData<User> get() = _user
    private val _user by lazy { MutableLiveData<User>() }

    override suspend fun fetchUser() {
        try {
            //TODO:handle 401
            val fetchedUser =
                if (!jwtProvider.getJWT().isNullOrEmpty()) treflorUserApiService.getUser(jwtProvider.getJWT()!!).await() else null
            _user.postValue(fetchedUser)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.")
        }
    }
}
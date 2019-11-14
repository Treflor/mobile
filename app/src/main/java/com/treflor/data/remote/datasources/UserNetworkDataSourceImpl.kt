package com.treflor.data.remote.datasources

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.data.remote.api.TreflorUserApiService
import com.treflor.internal.NoConnectivityException
import com.treflor.models.User

class UserNetworkDataSourceImpl(
    private val treflorUserApiService: TreflorUserApiService
) : UserNetworkDataSource {
    private val _user = MutableLiveData<User>()
    override val user: LiveData<User>
        get() = _user

    override suspend fun fetchUser() {
        try {
            val fetchedUser = treflorUserApiService.getUser().await()
            _user.postValue(fetchedUser)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}
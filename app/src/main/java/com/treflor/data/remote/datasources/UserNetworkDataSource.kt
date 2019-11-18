package com.treflor.data.remote.datasources

import androidx.lifecycle.LiveData
import com.treflor.models.User

interface UserNetworkDataSource {
    val user:LiveData<User>
    suspend fun fetchUser()
}
package com.treflor.data.remote.datasources

import com.treflor.data.remote.response.DirectionApiResponse

interface GoogleDirectionNetworkDataSource {
    suspend fun getDirection(origin:String,destination:String,mode:String):DirectionApiResponse?
}
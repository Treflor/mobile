package com.treflor.data.db.datasources

import androidx.lifecycle.LiveData
import com.treflor.data.remote.response.DirectionApiResponse

interface DirectionDBDataSource {
    val direction: LiveData<DirectionApiResponse>

    fun upsert(direction: DirectionApiResponse)
    fun delete()
}
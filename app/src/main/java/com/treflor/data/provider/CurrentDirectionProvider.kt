package com.treflor.data.provider

import androidx.lifecycle.LiveData
import com.treflor.models.directionapi.DirectionApiResponse

interface CurrentDirectionProvider {
    val currentDirection: LiveData<DirectionApiResponse>
    fun getCurrentDirection(): DirectionApiResponse?
    fun persistCurrentDirection(directionApiResponse: DirectionApiResponse)
    fun deleteDirection()
}
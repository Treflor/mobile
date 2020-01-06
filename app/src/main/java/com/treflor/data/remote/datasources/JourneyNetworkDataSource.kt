package com.treflor.data.remote.datasources

import com.treflor.data.remote.requests.JourneyRequest

interface JourneyNetworkDataSource {
    fun uploadJourney(journeyRequest: JourneyRequest)
}
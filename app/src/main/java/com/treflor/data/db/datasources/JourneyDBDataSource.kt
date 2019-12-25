package com.treflor.data.db.datasources

import androidx.lifecycle.LiveData
import com.treflor.models.Journey

interface JourneyDBDataSource {
    val journey: LiveData<Journey>

    fun upsert(journey: Journey)
    fun delete()
}
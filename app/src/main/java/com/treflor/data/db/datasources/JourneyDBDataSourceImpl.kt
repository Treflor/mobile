package com.treflor.data.db.datasources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.data.db.dao.JourneyDao
import com.treflor.models.Journey

class JourneyDBDataSourceImpl(
    private val journeyDao: JourneyDao
) : JourneyDBDataSource {

    override val journey: LiveData<Journey> get() = _journey
    private val _journey by lazy {
        MutableLiveData<Journey>(journeyDao.getJourney())
    }

    override fun upsert(journey: Journey) {
        journeyDao.upsert(journey)
        _journey.postValue(journey)
    }

    override fun delete() {
        journeyDao.delete()
        _journey.postValue(null)
    }
}
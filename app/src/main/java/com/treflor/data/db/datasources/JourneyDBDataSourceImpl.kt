package com.treflor.data.db.datasources

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.data.db.dao.JourneyDao
import com.treflor.data.db.dao.JourneyResponseDao
import com.treflor.data.remote.response.JourneyResponse
import com.treflor.models.Journey

class JourneyDBDataSourceImpl(
    private val journeyDao: JourneyDao,
    private val journeyResponseDao: JourneyResponseDao
) : JourneyDBDataSource {

    override val journey: LiveData<Journey> get() = _journey
    private val _journey by lazy {
        MutableLiveData<Journey>(journeyDao.getJourney())
    }

    override val JourneyResponses: LiveData<List<JourneyResponse>> get() = _journeyResponses
    private val _journeyResponses by lazy { MutableLiveData<List<JourneyResponse>>() }

    override fun upsert(journey: Journey) {
        journeyDao.upsert(journey)
        _journey.postValue(journey)
    }

    override fun delete() {
        journeyDao.delete()
        _journey.postValue(null)
    }

    override fun upsertAllJourneyResponses(journeys: List<JourneyResponse>) {
        journeyResponseDao.upsertAll(journeys)
        _journeyResponses.postValue(journeys)
    }

    override fun getAllJourneyResponses() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllJourneyResponses() {
        journeyResponseDao.deleteAll()
        _journeyResponses.postValue(null)
    }


}
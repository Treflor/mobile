package com.treflor.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.treflor.data.remote.response.JourneyResponse

@Dao
interface JourneyResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(journey: JourneyResponse)

    @Query("SELECT * FROM journey_responses;")
    fun getAllListJourneys(): LiveData<List<JourneyResponse>>


    @Query("SELECT * FROM journey_responses where user_id = :uid;")
    fun getAllUserJourneys(uid: String): LiveData<List<JourneyResponse>>


    @Query("SELECT * FROM journey_responses WHERE id = :id;")
    fun getDetailedJourneyById(id: String): LiveData<JourneyResponse>

    @Query("DELETE FROM journey_responses")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertAll(journeys: List<JourneyResponse>)
}
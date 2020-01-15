package com.treflor.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.treflor.data.remote.response.JourneyResponse

@Dao
interface JourneyResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(journey: JourneyResponse)

    @Query("SELECT * FROM journey_responses")
    fun getAllJourneys(): List<JourneyResponse>

    @Query("DELETE FROM journey_responses")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertAll(journeys: List<JourneyResponse>)
}
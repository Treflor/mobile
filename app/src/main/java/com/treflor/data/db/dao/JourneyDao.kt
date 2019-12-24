package com.treflor.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.treflor.models.CURRENT_JOURNEY_PK
import com.treflor.models.Journey

@Dao
interface JourneyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(journey: Journey)

    @Query("SELECT * FROM journey WHERE pk = $CURRENT_JOURNEY_PK")
    fun getJourney(): Journey

    @Query("DELETE FROM journey WHERE pk = $CURRENT_JOURNEY_PK")
    fun delete()

}
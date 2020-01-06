package com.treflor.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.treflor.models.TrackedLocation

@Dao
interface TrackedLocationsDao {

    @Insert
    fun insert(trackedLocation: TrackedLocation)

    @Query("SELECT * FROM tracked_locations")
    fun getLocations(): List<TrackedLocation>

    @Query("DELETE FROM tracked_locations")
    fun deleteTabel()

}
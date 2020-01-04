package com.treflor.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.treflor.data.remote.response.CURRENT_DIRECTION_PK
import com.treflor.data.remote.response.DirectionApiResponse

@Dao
interface DirectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(directionApiResponse: DirectionApiResponse)

    @Query("SELECT * FROM direction WHERE pk = $CURRENT_DIRECTION_PK")
    fun getDirection(): DirectionApiResponse

    @Query("DELETE FROM direction WHERE pk = $CURRENT_DIRECTION_PK")
    fun delete()
}
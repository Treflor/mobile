package com.treflor.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.treflor.models.CURRENT_USER_PK
import com.treflor.models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(user: User)

    @Query("SELECT * FROM user WHERE pk = $CURRENT_USER_PK")
    fun getUser(): User

    @Query("DELETE FROM user WHERE pk = $CURRENT_USER_PK")
    fun delete()
}


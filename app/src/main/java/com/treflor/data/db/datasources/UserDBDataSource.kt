package com.treflor.data.db.datasources

import androidx.lifecycle.LiveData
import com.treflor.models.User

interface UserDBDataSource {
    val user:LiveData<User>

    fun upsert(user: User)
    fun delete()
}
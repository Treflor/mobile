package com.treflor.data.db.datasources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.data.db.dao.DirectionDao
import com.treflor.data.remote.response.DirectionApiResponse

class DirectoionDBDataSourceImpl(
    private val directionDao: DirectionDao
) : DirectoionDBDataSource {
    override val direction: LiveData<DirectionApiResponse> get() = _direction

    private val _direction by lazy {
        MutableLiveData<DirectionApiResponse>(directionDao.getDirection())
    }

    override fun upsert(direction: DirectionApiResponse) {
        directionDao.upsert(direction)
        _direction.postValue(direction)
    }

    override fun delete() {
        directionDao.delete()
        _direction.postValue(null)
    }
}
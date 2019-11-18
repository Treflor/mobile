package com.treflor.data.db.datasources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.data.db.dao.UserDao
import com.treflor.models.User

class UserDBDataSourceImpl(
    private val userDao: UserDao
) : UserDBDataSource {

    override val user: LiveData<User> get() = _user
    private val _user by lazy { MutableLiveData<User>() }

    override fun upsert(user: User) {
        userDao.upsert(user)
        _user.postValue(user)
    }

    override fun delete() {
        userDao.delete()
        _user.postValue(null)
    }
}
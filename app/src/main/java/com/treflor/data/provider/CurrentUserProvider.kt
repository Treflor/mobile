package com.treflor.data.provider

import androidx.lifecycle.LiveData
import com.treflor.models.User

interface CurrentUserProvider {
    val currentUser: LiveData<User>
    fun getCurrentUser(): User?
    fun persistCurrentUser(user: User)
    fun deleteCurrentUser()
    fun getCurrentUserId():String?
}
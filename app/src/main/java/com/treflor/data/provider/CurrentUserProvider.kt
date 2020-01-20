package com.treflor.data.provider

import com.treflor.models.User

interface CurrentUserProvider {
    fun getCurrentUser():User?
    fun persistCurrentUser(user: User)
    fun deleteCurrentUser()
}
package com.treflor.data.repository

interface Repository {
    fun signInWithGoogle(accessToken: String)
}
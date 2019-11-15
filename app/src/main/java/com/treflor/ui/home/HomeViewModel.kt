package com.treflor.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.internal.AuthState
import com.treflor.internal.lazyDeferred
import com.treflor.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {
    val user by lazyDeferred { repository.getUser() }
}

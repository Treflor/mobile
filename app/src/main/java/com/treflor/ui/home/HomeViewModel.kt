package com.treflor.ui.home

import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.internal.lazyDeferred

class HomeViewModel(private val repository: Repository) : ViewModel() {

    val user by lazyDeferred {
        repository.getUser()
    }
}

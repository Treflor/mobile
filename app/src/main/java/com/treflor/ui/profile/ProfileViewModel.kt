package com.treflor.ui.profile

import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.internal.lazyDeferred

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    val user by lazyDeferred { repository.getUser() }
}

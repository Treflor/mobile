package com.treflor.ui.settings

import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.internal.lazyDeferred

class SettingsViewModel(private val repository: Repository) : ViewModel() {
    val user by lazyDeferred { repository.getUser() }
}

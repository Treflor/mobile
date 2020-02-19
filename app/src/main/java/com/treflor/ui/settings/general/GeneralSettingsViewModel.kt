package com.treflor.ui.settings.general

import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.internal.lazyDeferred

class GeneralSettingsViewModel(private val repository: Repository) : ViewModel() {
    val user by lazyDeferred { repository.getUser() }
}

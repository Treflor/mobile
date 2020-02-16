package com.treflor.ui.profile.journeys

import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.internal.lazyDeferred

class UserJourneyViewModel(private val repository: Repository) : ViewModel() {
    val user by lazyDeferred { repository.getUser() }
    val journeys by lazyDeferred { repository.userJourneys() }
}

package com.treflor.ui.profile.images

import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.internal.lazyDeferred

class UserImagesViewModel(private val repository: Repository) : ViewModel() {
    val journeys by lazyDeferred { repository.userJourneys() }
    val userId by lazy { repository.getCurrentUserId() }
}

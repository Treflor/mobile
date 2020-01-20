package com.treflor.ui.home.detailed

import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.internal.lazyDeferred

class JourneyDetailsViewModel(
    private val repository: Repository,
    private val id: String
) : ViewModel() {

    val journey by lazyDeferred {
        repository.getJourneyById(id)
    }
}

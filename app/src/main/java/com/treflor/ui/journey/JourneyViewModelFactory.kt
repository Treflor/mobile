package com.treflor.ui.journey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.treflor.data.repository.Repository

class JourneyViewModelFactory(
    private val repository: Repository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return JourneyViewModel(repository) as T
    }
}
package com.treflor.ui.home.journey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.treflor.data.repository.Repository

class JourneyDetailsViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return JourneyDetailsViewModel(repository) as T
    }
}
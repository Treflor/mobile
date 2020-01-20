package com.treflor.ui.home.detailed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.treflor.data.repository.Repository

class JourneyDetailsViewModelFactory(
    private val repository: Repository,
    private val id:String
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return JourneyDetailsViewModel(repository,id) as T
    }
}
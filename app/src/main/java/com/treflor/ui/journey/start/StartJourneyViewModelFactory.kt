package com.treflor.ui.journey.start

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.treflor.data.repository.Repository

class StartJourneyViewModelFactory(
    private val repository: Repository,
    private val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StartJourneyViewModel(repository, context) as T
    }
}
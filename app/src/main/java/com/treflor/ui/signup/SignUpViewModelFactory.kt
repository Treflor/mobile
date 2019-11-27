package com.treflor.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.treflor.data.repository.Repository

class SignUpViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignUpViewModel(repository) as T
    }
}
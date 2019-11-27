package com.treflor.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.data.repository.Repository
import com.treflor.internal.eventexcecutor.ActivityNavigation
import com.treflor.internal.eventexcecutor.LiveMessageEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val repository: Repository
) : ViewModel() {

    val signingIn: LiveData<Boolean> get() = _signingIn
    private val _signingIn = MutableLiveData<Boolean>(false)

    // for send events to fragment
    val liveMessageEvent =
        LiveMessageEvent<ActivityNavigation>()

    fun signUp(request: SignUpRequest) {
//     TODO:   validateData()
        _signingIn.postValue(true)
        repository.signUp(request)
        // now we can navigate to profile
        GlobalScope.launch(Dispatchers.Main) { liveMessageEvent.sendEvent { navigateUp() } }
        _signingIn.postValue(false)
    }
}

package com.treflor.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.data.repository.Repository
import com.treflor.internal.SignUpState
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
        _signingIn.postValue(true)
        GlobalScope.launch(Dispatchers.IO) {
            when (repository.signUp(request)) {
                SignUpState.EMAIL_ALL_READY -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        liveMessageEvent.sendEvent { showSnackBar("Email already registered!") }
                    }
                }
                SignUpState.ERROR -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        liveMessageEvent.sendEvent { showSnackBar("Something went wrong!") }
                    }
                }
                SignUpState.DONE -> {
                    // now we can navigate to profile
                    GlobalScope.launch(Dispatchers.Main) {
                        liveMessageEvent.sendEvent { showSnackBar("Signed up as: ${request.firstName} ${request.lastName}") }
                        liveMessageEvent.sendEvent { navigateUp() }
                    }
                }
            }
        }
        _signingIn.postValue(false)
    }
}

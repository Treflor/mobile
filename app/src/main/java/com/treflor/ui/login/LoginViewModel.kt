package com.treflor.ui.login

import android.content.Context
import android.content.Intent
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.treflor.internal.ActivityNavigation
import com.treflor.internal.LiveMessageEvent

private const val GOOGLE_SIGN_IN = 9001

class LoginViewModel(private val context: Context) : ViewModel() {

    val startActivityForResultEvent = LiveMessageEvent<ActivityNavigation>()
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    private val googleSignInClient = GoogleSignIn.getClient(context, gso)

    @BindingAdapter("android:onClick")
    fun signInWithGoogle() {
        //TODO inject
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResultEvent.sendEvent {
            startActivityForResult(
                signInIntent,
                GOOGLE_SIGN_IN
            )
        }
    }

    fun onResultFromActivity(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GOOGLE_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                googleSignInComplete(task)
            }
        }
    }

    private fun googleSignInComplete(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.apply {
                // .. Store user details
//                emitUiState(
//                    showSuccess = Event(R.string.login_successful)
//                )
            }
        } catch (e: ApiException) {
//            emitUiState(
//                showError = Event(R.string.login_failed)
//            )
        }
    }


}
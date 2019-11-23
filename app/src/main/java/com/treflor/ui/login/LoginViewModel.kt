package com.treflor.ui.login

import android.accounts.Account
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.treflor.data.repository.Repository
import com.treflor.internal.eventexcecutor.ActivityNavigation
import com.treflor.internal.eventexcecutor.LiveMessageEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

private const val GOOGLE_SIGN_IN = 9001

class LoginViewModel(
    private val context: Context,
    private val repository: Repository
) : ViewModel() {

    val signingIn: LiveData<Boolean> get() = _signingIn
    private val _signingIn = MutableLiveData<Boolean>(false)

    // for send events to fragment
    val liveMessageEvent =
        LiveMessageEvent<ActivityNavigation>()

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestProfile()
        .requestId()
        .build()
    private val googleSignInClient = GoogleSignIn.getClient(context, gso)

    fun signInWithGoogle() {
        // show the progress bar
        _signingIn.postValue(true)

        // sending sign in event from emails
        val signInIntent = googleSignInClient.signInIntent
        liveMessageEvent.sendEvent {
            startActivityForResult(
                signInIntent,
                GOOGLE_SIGN_IN
            )
        }
    }

    fun onResultFromActivity(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GOOGLE_SIGN_IN -> {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    googleSignInComplete(task)
                }
            }
        } else {
            _signingIn.postValue(false)
        }
    }

    private fun googleSignInComplete(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.apply {
                // access token according to the account
                requestAccessToken(account.account)
            }
        } catch (e: ApiException) {
            println("exception? $e")
        }
    }

    private fun requestAccessToken(account: Account?) = GlobalScope.launch(Dispatchers.IO) {
        val scope =
            "oauth2:https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.profile"

        val accessToken = try {
            GoogleAuthUtil.getToken(context, account, scope)
        } catch (e: IOException) {
            //TODO: show the stupid user to no connection
            // note send it through LiveMessage event to view
            Log.e("Google access token", "no connection")
            null
        }
        if (accessToken != null) {
            repository.signInWithGoogle(accessToken)
            // now we can navigate to profile
            GlobalScope.launch(Dispatchers.Main) { liveMessageEvent.sendEvent { navigateUp() } }
        }
        _signingIn.postValue(false)
    }

}
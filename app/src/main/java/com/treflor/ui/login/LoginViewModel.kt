package com.treflor.ui.login

import android.accounts.Account
import android.content.Context
import android.content.Intent
import androidx.databinding.BindingAdapter
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

    val startActivityForResultEvent =
        LiveMessageEvent<ActivityNavigation>()
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestProfile()
        .requestId()
        .build()
    private val googleSignInClient = GoogleSignIn.getClient(context, gso)

    @BindingAdapter("android:onClick")
    fun signInWithGoogle() {
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
                requestAccessToken(account.account)
                startActivityForResultEvent.sendEvent { navigateUp() }
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
            //TODO: show the stupid your to no connection
            // note send it through LiveMessage event to view
            println("no connection")
            null
        }
        if (accessToken != null) repository.signInWithGoogle(accessToken)
    }


}
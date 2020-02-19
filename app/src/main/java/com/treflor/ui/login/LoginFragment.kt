package com.treflor.ui.login


import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar

import com.treflor.R
import com.treflor.internal.eventexcecutor.ActivityNavigation
import kotlinx.android.synthetic.main.fragment_login.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment(), KodeinAware,
    ActivityNavigation, View.OnClickListener {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: LoginViewModelFactory by instance()
    lateinit var viewModel: LoginViewModel
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LoginViewModel::class.java)

        viewModel.liveMessageEvent.setEventReceiver(this, this)

        bindUI()
    }

    private fun bindUI() {
        viewModel.signingIn.observe(this, Observer { signingIn ->
            progress_bar.visibility = if (signingIn) View.VISIBLE else View.INVISIBLE
            btn_google_sign_in.isEnabled = !signingIn
            btn_sign_in.isEnabled = !signingIn
            btn_sign_up.isEnabled = !signingIn
        })
        btn_sign_up.setOnClickListener(this)
        btn_sign_in.setOnClickListener(this)
        btn_google_sign_in.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onResultFromActivity(requestCode, resultCode, data)
    }

    override fun navigateUp(): Boolean = navController.navigateUp()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_sign_up -> {
                navController.navigate(R.id.action_loginFragment_to_signUpFragment)
            }
            R.id.btn_sign_in -> {
                if (validate()) {
                    viewModel.signIn(txt_email.text.toString(), txt_password.text.toString())
                }
            }
            R.id.btn_google_sign_in -> {
                viewModel.signInWithGoogle()
            }
        }
    }

    private fun validate(): Boolean {
        til_email.error = ""

        var valid = true
        if (txt_email.text.isNullOrEmpty()) {
            til_email.error = "Email can't be empty!"
            valid = false
        } else {
            val pattern = Patterns.EMAIL_ADDRESS
            if (!pattern.matcher(txt_email.text).matches()) {
                til_email.error = "Email not in valid format!"
                valid = false
            }
        }

        if (txt_password.text.isNullOrEmpty()) {
            til_password.error = "Password can't be empty!"
            valid = false
        }
        return valid
    }

    override fun showSnackBar(s: String) {
        if (view != null) Snackbar.make(view!!, s, Snackbar.LENGTH_SHORT).show()
    }
}

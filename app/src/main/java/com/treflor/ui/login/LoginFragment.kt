package com.treflor.ui.login


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.treflor.R
import com.treflor.databinding.FragmentLoginBinding
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

        val loginFragmentBinding: FragmentLoginBinding =
            FragmentLoginBinding.bind(view)
        loginFragmentBinding.viewModel = viewModel

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
                viewModel.signIn(txt_email.text.toString(), txt_password.text.toString())
            }
        }
    }
}

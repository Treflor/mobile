package com.treflor.ui.login


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    ActivityNavigation ,View.OnClickListener{

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

        viewModel.startActivityForResultEvent.setEventReceiver(this, this)
        btn_sign_up.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onResultFromActivity(requestCode, resultCode, data)
    }

    override fun navigateUp(): Boolean = navController.navigateUp()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_sign_up ->{
                print("am i working?")
                navController.navigate(R.id.action_loginFragment_to_signUpFragment)}
        }
    }
}

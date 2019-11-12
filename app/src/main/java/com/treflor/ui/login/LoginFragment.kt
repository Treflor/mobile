package com.treflor.ui.login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.treflor.R
import com.treflor.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: LoginViewModelFactory by instance()
    lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LoginViewModel::class.java)
        val loginFragmentBinding: FragmentLoginBinding =
            FragmentLoginBinding.inflate(inflater, container, false)
        loginFragmentBinding.viewModel = viewModel
        return loginFragmentBinding.root

    }
}

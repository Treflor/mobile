package com.treflor.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.treflor.R
import com.treflor.databinding.HomeFragmentBinding
import com.treflor.internal.ui.base.TreflorScopedFragment
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class HomeFragment : TreflorScopedFragment(), View.OnClickListener, KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: HomeViewModelFactory by instance()

    private lateinit var navController: NavController
    private lateinit var viewModel: HomeViewModel
    private lateinit var homeFragmentBinding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        account_icon.setOnClickListener(this)
        homeFragmentBinding =
            HomeFragmentBinding.bind(view)
        bindUI()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.account_icon -> navController.navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }

    private fun bindUI() = launch {
        val user = viewModel.user.await()
        user.observe(this@HomeFragment, Observer {
            if (it == null) return@Observer
            homeFragmentBinding.user = it
        })
    }
}

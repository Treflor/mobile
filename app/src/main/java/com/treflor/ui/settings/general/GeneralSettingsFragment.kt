package com.treflor.ui.settings.general

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide

import com.treflor.internal.ui.base.TreflorScopedFragment
import com.treflor.models.User
import kotlinx.android.synthetic.main.general_settings_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class GeneralSettingsFragment : TreflorScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: GeneralSettingsViewModelFactory by instance()

    private lateinit var viewModel: GeneralSettingsViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.treflor.R.layout.general_settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(GeneralSettingsViewModel::class.java)
        bindUI()

    }

    private fun bindUI() = launch {
        val user = viewModel.user.await()
        user.observe(this@GeneralSettingsFragment, Observer {
            if (it == null) return@Observer
            profilePicture(it)
        })
    }

    private fun profilePicture(user: User) {
        Glide.with(this@GeneralSettingsFragment)
            .load(user.photo)
            .centerCrop()
            .into(img_profile_picture)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GeneralSettingsViewModel::class.java)
    }

}

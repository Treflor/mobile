package com.treflor.ui.settings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.treflor.R
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class SettingsFragment : Fragment(), View.OnClickListener , KodeinAware {

    override val kodein: Kodein by closestKodein()
    private lateinit var viewModel: SettingsViewModel

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        edit_profile.setOnClickListener(this)
        change_password.setOnClickListener(this)
        privacy.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v!!.id) {
            R.id.edit_profile -> navController.navigate(R.id.action_settingsFragment_to_generalSettings)
            R.id.change_password -> navController.navigate(R.id.action_settingsFragment_to_securitySettings)
            R.id.privacy -> navController.navigate(R.id.action_settingsFragment_to_privacyPolicyFragment)
            R.id.about -> navController.navigate(R.id.action_settingsFragment_to_aboutAppFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

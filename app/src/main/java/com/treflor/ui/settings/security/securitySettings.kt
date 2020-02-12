package com.treflor.ui.settings.security

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.treflor.R

class securitySettings : Fragment() {

    companion object {
        fun newInstance() = securitySettings()
    }

    private lateinit var viewModel: SecuritySettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.security_settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SecuritySettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

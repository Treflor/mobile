package com.treflor.ui.settings.general

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.treflor.R

class generalSettings : Fragment() {

    companion object {
        fun newInstance() = generalSettings()
    }

    private lateinit var viewModel: GeneralSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.general_settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GeneralSettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

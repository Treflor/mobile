package com.treflor.ui.settings.privacy


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController

import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_privacy_policy.*


/**
 * A simple [Fragment] subclass.
 */
class PrivacyPolicyFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.treflor.R.layout.fragment_privacy_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


}
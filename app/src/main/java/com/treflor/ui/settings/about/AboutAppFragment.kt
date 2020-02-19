package com.treflor.ui.settings.about


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController

import com.treflor.R
import kotlinx.android.synthetic.main.fragment_about_app.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

/**
 * A simple [Fragment] subclass.
 */
class AboutAppFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_app, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Home up action
        (activity as AppCompatActivity).setSupportActionBar(about_toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


}

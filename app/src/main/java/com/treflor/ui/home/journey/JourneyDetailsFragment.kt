package com.treflor.ui.home.journey

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.treflor.R
import com.treflor.internal.ui.base.TreflorScopedFragment
import kotlinx.android.synthetic.main.journey_details_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class JourneyDetailsFragment : TreflorScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: JourneyDetailsViewModelFactory by instance()

    private lateinit var viewModel: JourneyDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.journey_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(JourneyDetailsViewModel::class.java)

        bindUI()
    }

    private fun bindUI() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }


}

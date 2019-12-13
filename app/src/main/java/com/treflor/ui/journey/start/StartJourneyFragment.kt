package com.treflor.ui.journey.start

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.treflor.R

class StartJourneyFragment : Fragment() {

    companion object {
        fun newInstance() = StartJourneyFragment()
    }

    private lateinit var viewModel: StartJourneyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_journey_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StartJourneyViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

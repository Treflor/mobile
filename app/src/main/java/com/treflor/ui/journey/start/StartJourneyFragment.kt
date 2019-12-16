package com.treflor.ui.journey.start

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.treflor.R


const val AUTOCOMPLETE_REQUEST_CODE = 321

class StartJourneyFragment : Fragment(), View.OnClickListener, PlaceSelectionListener {

    private lateinit var navController: NavController
    private lateinit var viewModel: StartJourneyViewModel
    private val fields =
        listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
    private lateinit var placeAutoCompleteFragment: AutocompleteSupportFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_journey_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        bindUI()
    }

    private fun bindUI() {
        Places.initialize(this.context!!, getString(R.string.google_api_key))
        placeAutoCompleteFragment =childFragmentManager.findFragmentById(R.id.places_fragment) as AutocompleteSupportFragment
        placeAutoCompleteFragment.setPlaceFields(fields)
        placeAutoCompleteFragment.setOnPlaceSelectedListener(this)
        placeAutoCompleteFragment.setActivityMode(AutocompleteActivityMode.OVERLAY)

    }

    override fun onClick(v: View?) {

    }

    override fun onPlaceSelected(place: Place) {
        Log.e("selected", "${place.name} ${place.address}, ${place.latLng}")
    }

    override fun onError(status: Status) {
        Log.e("err", "${status.statusMessage}")
    }


}

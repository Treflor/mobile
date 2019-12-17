package com.treflor.ui.journey.start

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.treflor.R
import kotlinx.android.synthetic.main.start_journey_fragment.*


const val AUTOCOMPLETE_REQUEST_CODE = 321
const val CURRENT_PLACE_AUTOCOMPLETE_REQUEST_CODE = 322

class StartJourneyFragment : Fragment(), View.OnClickListener, PlaceSelectionListener {

    private lateinit var navController: NavController
    private lateinit var viewModel: StartJourneyViewModel
    private val fields =
        listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

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

        et_current_place.setOnClickListener(this)
        et_destination_places.setOnClickListener(this)

        setCurrentPlace()
    }

    private fun setCurrentPlace() {
        //TODO: set a loading
        val request = FindCurrentPlaceRequest.newInstance(fields)
        if (ContextCompat.checkSelfPermission(
                this.context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val placeResponse = Places.createClient(this.context!!).findCurrentPlace(request)
            placeResponse.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    et_current_place.setText(
                        task.result?.placeLikelihoods?.first()?.place?.name ?: ""
                    )
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        Log.e("Exception", "Place not found ex: ${exception.statusCode}")
                    }
                }
            }
        } else {
            // TODO: request permissions
            // See https://developer.android.com/training/permissions/requesting

        }
    }

    private fun getPlace(requestCode: Int) {
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(this.context!!)
        startActivityForResult(intent, requestCode)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.et_current_place -> {
                getPlace(CURRENT_PLACE_AUTOCOMPLETE_REQUEST_CODE)
            }

            R.id.et_destination_places -> {
                getPlace(AUTOCOMPLETE_REQUEST_CODE)
            }
        }
    }

    override fun onPlaceSelected(place: Place) {
        Log.e("selected", "${place.name} ${place.address}, ${place.latLng}")
    }

    override fun onError(status: Status) {
        Log.e("err", "${status.statusMessage}")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CURRENT_PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    et_current_place.setText(place.name)
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    val status =
                        Autocomplete.getStatusFromIntent(data!!)
                    Log.i("ERROR", "error: ${status.statusMessage}")

                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                    //TODO: handle this
                }
            }

        }
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    et_destination_places.setText(place.name)
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    val status =
                        Autocomplete.getStatusFromIntent(data!!)
                    Log.i("ERROR", "error: ${status.statusMessage}")

                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                    //TODO: handle this
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}

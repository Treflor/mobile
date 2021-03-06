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
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.treflor.R
import com.treflor.internal.eventexcecutor.ActivityNavigation
import com.treflor.internal.imageToBase64
import com.treflor.models.Journey
import com.treflor.models.TreflorPlace
import kotlinx.android.synthetic.main.start_journey_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


const val AUTOCOMPLETE_REQUEST_CODE = 321
const val CURRENT_PLACE_AUTOCOMPLETE_REQUEST_CODE = 322

class StartJourneyFragment : Fragment(), View.OnClickListener, PlaceSelectionListener, KodeinAware,
    ActivityNavigation {

    override val kodein: Kodein by closestKodein()
    private lateinit var navController: NavController
    private lateinit var viewModel: StartJourneyViewModel
    private val viewModelFactory: StartJourneyViewModelFactory by instance()
    private val fields =
        listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
    private var startLocation: TreflorPlace? = null
    private var destination: TreflorPlace? = null
    private var base64Image: String? = null
    private var colors = listOf(
        android.R.color.holo_red_light,
        android.R.color.holo_blue_light,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_purple
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_journey_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(StartJourneyViewModel::class.java)
        bindUI()
    }

    private fun bindUI() {
        Places.initialize(this.context!!, getString(R.string.google_api_key))

        spinner_difficulty.adapter = object : ArrayAdapter<String>(
            this.context!!, android.R.layout.simple_spinner_dropdown_item,
            listOf("Easy", "Medium", "Hard", "Level")
        ) {
            override fun getCount(): Int = 3
        }
        spinner_difficulty.setSelection(3)
        et_labels.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val labelText = v.text.toString()
                if (!labelText.isNullOrEmpty()) {
                    addChipToTags(labelText)
                    et_labels.setText("")
                }
                return@setOnEditorActionListener true
            }
            false
        }

        add_image.setOnClickListener(this)
        et_current_place.setOnClickListener(this)
        et_destination_place.setOnClickListener(this)
        fab_start_journey.setOnClickListener(this)

        setCurrentPlace()
    }

    private fun addChipToTags(labelText: String) {
        val chip = Chip(this.context)
        chip.text = labelText
        chip.isCloseIconVisible = true
        chip.isClickable = false
        chip.isCheckable = false
        chip.setChipBackgroundColorResource(colors.random())
        chip_group_labels.addView(chip as View)
        chip.setOnCloseIconClickListener { chip_group_labels.removeView(chip as View) }
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
                    val currentPlace = task.result?.placeLikelihoods?.first()?.place
                    et_current_place.setText(currentPlace?.name ?: "")
                    startLocation = TreflorPlace(currentPlace)
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
            R.id.add_image -> {
                ImagePicker.create(this@StartJourneyFragment)
                    .returnMode(ReturnMode.ALL)
                    .folderMode(true)
                    .toolbarImageTitle("Upload an Image")
                    .single()
                    .imageDirectory("treflor")
                    .start()
            }
            R.id.et_current_place -> {
                getPlace(CURRENT_PLACE_AUTOCOMPLETE_REQUEST_CODE)
            }

            R.id.et_destination_place -> {
                getPlace(AUTOCOMPLETE_REQUEST_CODE)
            }
            R.id.fab_start_journey -> {
                val title = et_title.text.toString()
                val content = et_content.text.toString()
                val level = spinner_difficulty.selectedItem.toString()
                val labels =
                    chip_group_labels.children.map { view -> (view as Chip).text.toString() }
                        .toList()
                if (title.isEmpty()) {
                    showSnackBar("Please enter a title!")
                    return
                }
                if (content.isEmpty()) {
                    showSnackBar("Please enter a content!")
                    return
                }
                if (startLocation == null) {
                    showSnackBar("Please select a starting location!")
                    return
                }
                if (destination == null) {
                    showSnackBar("Please select a ending location!")
                    return
                }
                if (level == "Level") {
                    showSnackBar("Please select a difficulty Level!")
                    return
                }
                if (labels.isEmpty()) {
                    showSnackBar("Please enter one or more labels!")
                    return
                }
                if (base64Image.isNullOrEmpty()) {
                    showSnackBar("Please select an image!")
                    return
                }
                viewModel.startJourney(
                    Journey(
                        "",                     //will issue by server
                        et_title.text.toString(),
                        et_content.text.toString(),
                        startLocation!!,
                        destination!!,
                        level,
                        labels,
                        base64Image!!
                    )
                )
                navController.navigate(R.id.action_startJourneyFragment_to_journeyFragment)
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
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val image = ImagePicker.getFirstImageOrNull(data)

//                load image in to imageview
            add_image.setPadding(0, 0, 0, 0)
            Glide.with(this@StartJourneyFragment)
                .load(image.path)
                .centerCrop()
                .into(add_image)
            add_image.updatePadding(top = 0, bottom = 0)

//            set image in request
            base64Image = imageToBase64(image.path)
        }
        if (requestCode == CURRENT_PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    et_current_place.setText(place.name)
                    startLocation = TreflorPlace(place)
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
                    et_destination_place.setText(place.name)
                    destination = TreflorPlace(place)
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

    override fun navigateUp(): Boolean = navController.navigateUp()

    override fun showSnackBar(s: String) {
        if (view != null) Snackbar.make(view!!, s, Snackbar.LENGTH_SHORT).show()
    }
}
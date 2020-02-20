package com.treflor.ui.journey

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.OnMapReadyCallback
import com.google.android.libraries.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.treflor.R
import com.treflor.internal.eventexcecutor.ActivityNavigation
import com.treflor.internal.imageToBase64
import com.treflor.internal.ui.base.TreflorScopedFragment
import com.treflor.models.Landmark
import com.treflor.ui.journey.bottomsheets.LandmarkBottomSheetDialog
import kotlinx.android.synthetic.main.journey_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class JourneyFragment : TreflorScopedFragment(), OnMapReadyCallback, KodeinAware,
    ActivityNavigation {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: JourneyViewModelFactory by instance()
    private lateinit var navController: NavController
    private lateinit var viewModel: JourneyViewModel
    private var myPositionMarker: Marker? = null
    private var endLocationJourneyMarker: Marker? = null
    private var landmarkPicker: Marker? = null
    private var googleMap: GoogleMap? = null
    private var camPosUpdatedOnFirstLaunch = false
    private var routePolyline: Polyline? = null
    private var trackedPolyline: Polyline? = null
    private val landmarks: MutableList<Marker?> = mutableListOf()

    private lateinit var icLandmarkB: Bitmap
    private lateinit var icLandmarkOnMoveB: Bitmap
    private lateinit var icLandmarkSmall: Bitmap
    private lateinit var icLandmarkOnMoveSmall: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.journey_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(JourneyViewModel::class.java)
        checkPermissions(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    private fun checkPermissions(savedInstanceState: Bundle?) {
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report != null) {
                        if (report.areAllPermissionsGranted()) {
                            bindUI(savedInstanceState)
                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            journey_map.visibility = View.GONE
                            txt_permission.visibility = View.VISIBLE
                        }
                    } else {
                        journey_map.visibility = View.GONE
                        txt_permission.visibility = View.VISIBLE
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                }
            })
            .check()
    }

    private fun bindUI(savedInstanceState: Bundle?) = launch {
        icLandmarkB = BitmapFactory.decodeResource(resources, R.drawable.landmark_picker)
        icLandmarkOnMoveB =
            BitmapFactory.decodeResource(resources, R.drawable.landmark_picker_on_move)
        icLandmarkSmall = Bitmap.createScaledBitmap(icLandmarkB, 70, 70, false)
        icLandmarkOnMoveSmall = Bitmap.createScaledBitmap(icLandmarkOnMoveB, 70, 70, false)

        viewModel.liveMessageEvent.setEventReceiver(this@JourneyFragment, this@JourneyFragment)
        journey_map.onCreate(savedInstanceState)
        journey_map.getMapAsync(this@JourneyFragment)

        viewModel.location.observe(this@JourneyFragment, Observer {
            if (it != null) {
                myPositionMarker?.position = LatLng(it.latitude, it.longitude)
                if (!camPosUpdatedOnFirstLaunch) {
                    camPosUpdatedOnFirstLaunch = true
                    googleMap?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                it.latitude,
                                it.longitude
                            ), 15f
                        )
                    )
                }
            }
        })

        viewModel.journey.await().observe(this@JourneyFragment, Observer {
            if (it == null) {
                btn_start_journey.setImageResource(R.drawable.ic_hiking)
                btn_start_journey.setOnClickListener { navController.navigate(R.id.action_journeyFragment_to_startJourneyFragment) }

                btn_add_landmark.visibility = View.GONE
                btn_add_landmark.setOnClickListener(null)

                btn_my_location.visibility = View.GONE
                btn_my_location.setOnClickListener(null)

                btn_upload_images.visibility = View.GONE
                btn_upload_images.setOnClickListener(null)

                endLocationJourneyMarker?.remove()
                endLocationJourneyMarker = null
                landmarks.forEach { marker -> marker?.remove() }
                landmarks.clear()
            } else {
                btn_start_journey.setImageResource(R.drawable.ic_finish_journey)
                btn_start_journey.setOnClickListener { viewModel.finishJourney() }
                btn_add_landmark.visibility = View.VISIBLE
                btn_add_landmark.setOnClickListener {
                    if (landmarkPicker == null) {
                        landmarkPicker = googleMap?.addMarker(
                            MarkerOptions()
                                .title("Landmark")
                                .snippet("Place on the landmark position")
                                .icon(BitmapDescriptorFactory.fromBitmap(icLandmarkSmall))
                                .position(googleMap?.cameraPosition?.target)
                        )
                        btn_add_landmark.setImageResource(R.drawable.ef_ic_done_white)
                    } else {
                        val landmarkBottomSheet = LandmarkBottomSheetDialog()
                        landmarkBottomSheet.listener =
                            object : LandmarkBottomSheetDialog.LandmarkBottomSheetListener {
                                override fun onDismiss(dialog: DialogInterface) {

                                    btn_add_landmark.setImageResource(R.drawable.ic_add_location_white_24dp)
                                    landmarkPicker?.remove()
                                    landmarkPicker = null
                                }

                                override fun onSave(
                                    title: String,
                                    snippet: String,
                                    type: String,
                                    imagesPaths: List<String>?,
                                    bottomSheetDialog: LandmarkBottomSheetDialog
                                ) {

                                    viewModel.persistLandmark(
                                        Landmark(
                                            "",
                                            title,
                                            snippet,
                                            type,
                                            imagesPaths?.map { path -> imageToBase64(path) }?.toList(),
                                            landmarkPicker!!.position.latitude,
                                            landmarkPicker!!.position.longitude
                                        )
                                    )
                                    bottomSheetDialog.dismiss()
                                }
                            }
                        landmarkBottomSheet.show(activity!!.supportFragmentManager, "landmark")
                    }
                }

                btn_my_location.visibility = View.VISIBLE
                btn_my_location.setOnClickListener {
                    googleMap?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            myPositionMarker?.position,
                            17f
                        )
                    )
                }

                btn_upload_images.visibility = View.VISIBLE
                btn_upload_images.setOnClickListener {
                    ImagePicker.create(this@JourneyFragment)
                        .folderMode(true)
                        .toolbarImageTitle("Select a Profile Image")
                        .multi()
                        .limit(10)
                        .imageDirectory("treflor")
                        .start()
                }

                val icEndLandmarkB = BitmapFactory.decodeResource(resources, R.drawable.flag)
                val icEndLandmarkSmall =
                    Bitmap.createScaledBitmap(icEndLandmarkB, 70, 70, false)
                endLocationJourneyMarker = googleMap?.addMarker(
                    MarkerOptions()
                        .title(it.destination.name)
                        .icon(BitmapDescriptorFactory.fromBitmap(icEndLandmarkSmall))
                        .snippet(it.destination.address)
                        .position(LatLng(it.destination.latitude, it.destination.longitude))
                )

            }
        })

        viewModel.direction.await().observe(this@JourneyFragment, Observer {
            routePolyline?.remove()
            if (it != null) {
                routePolyline = googleMap?.addPolyline(PolylineOptions().addAll(it.decodedPoints()))
                googleMap?.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        it.getLatLngBounds(),
                        30
                    )
                )
            }
        })

        viewModel.trackedLocations.await().observe(this@JourneyFragment, Observer {
            trackedPolyline?.remove()
            if (it != null) {
                trackedPolyline = googleMap?.addPolyline(
                    PolylineOptions()
                        .addAll(it.map { tl -> LatLng(tl.lat, tl.lng) }.toList())
                        .color(Color.GREEN)
                        .zIndex(2f)
                )
            }
        })

        viewModel.landmarks.observe(this@JourneyFragment, Observer {
            landmarks.forEach { marker -> marker?.remove() }
            landmarks.clear()
            val icLandmarkPinB = BitmapFactory.decodeResource(resources, R.drawable.landmark_pin)
            val icLandmarkPinSmall = Bitmap.createScaledBitmap(icLandmarkPinB, 70, 70, false)

            it?.map { landmark ->
                MarkerOptions()
                    .title(landmark.title)
                    .snippet(landmark.snippet)
                    .icon(BitmapDescriptorFactory.fromBitmap(icLandmarkPinSmall))
                    .position(landmark.toLatLng())
            }?.toList()
                ?.forEach { markerOption -> landmarks.add(googleMap?.addMarker(markerOption)) }

        })
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        map?.uiSettings?.isMapToolbarEnabled = false
        val b = BitmapFactory.decodeResource(resources, R.drawable.ic_hiking)
        val smallMarker = Bitmap.createScaledBitmap(b, 70, 70, false)
        myPositionMarker = googleMap?.addMarker(
            MarkerOptions()
                .title("My location")
                .snippet("I am here")
                .position(
                    LatLng(
                        viewModel.location.value?.latitude ?: 0.0,
                        viewModel.location.value?.longitude ?: 0.0
                    )
                )
                .icon(
                    BitmapDescriptorFactory.fromBitmap(smallMarker)
                )
        )
        viewModel.location.value?.let {
            camPosUpdatedOnFirstLaunch = true
            googleMap?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        it.latitude,
                        it.longitude
                    ), 15f
                )
            )
        }

        googleMap?.setOnCameraMoveListener {
            landmarkPicker?.position = googleMap?.cameraPosition?.target
        }
        googleMap?.setOnCameraMoveStartedListener {
            landmarkPicker?.setIcon(BitmapDescriptorFactory.fromBitmap(icLandmarkOnMoveSmall))
        }
        googleMap?.setOnCameraIdleListener {
            landmarkPicker?.setIcon(BitmapDescriptorFactory.fromBitmap(icLandmarkSmall))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            var images = ImagePicker.getImages(data)
            viewModel.addImagesToJourney(images)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        viewModel.removeLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestLocationUpdates()
    }

    override fun navigateUp(): Boolean = true
    override fun showSnackBar(s: String) {
        if (view != null) Snackbar.make(view!!, s, Snackbar.LENGTH_SHORT).show()
    }


}

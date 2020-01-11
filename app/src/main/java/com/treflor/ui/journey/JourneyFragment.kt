package com.treflor.ui.journey

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
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
import com.treflor.internal.ui.base.TreflorScopedFragment
import com.treflor.models.Journey
import kotlinx.android.synthetic.main.journey_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class JourneyFragment : TreflorScopedFragment(), OnMapReadyCallback, KodeinAware,
    View.OnClickListener, ActivityNavigation {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: JourneyViewModelFactory by instance()
    private lateinit var navController: NavController
    private lateinit var viewModel: JourneyViewModel
    private var myPositionMarker: Marker? = null
    private var googleMap: GoogleMap? = null
    private var camPosUpdatedOnFirstLaunch = false
    private var routePolyline: Polyline? = null
    private var trackedPolyline: Polyline? = null
    private var journey: Journey? = null

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
        viewModel.liveMessageEvent.setEventReceiver(this@JourneyFragment, this@JourneyFragment)
        journey_map.onCreate(savedInstanceState)
        journey_map.getMapAsync(this@JourneyFragment)

        viewModel.location.observe(this@JourneyFragment, Observer {
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
        })

        viewModel.journey.await().observe(this@JourneyFragment, Observer {
            journey = it
            if (it == null) {
                btn_start_journey.setImageResource(R.drawable.ic_hiking)
            } else {
                btn_start_journey.setImageResource(R.drawable.ic_home)
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
        btn_start_journey.setOnClickListener(this@JourneyFragment)
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        map?.uiSettings?.isMapToolbarEnabled = false
        val b = BitmapFactory.decodeResource(resources, R.drawable.ic_hiking);
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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_start_journey -> {
                if (journey != null) {
                    viewModel.finishJourney()
                } else {
                    navController.navigate(R.id.action_journeyFragment_to_startJourneyFragment)
                }
            }
        }
    }

    override fun navigateUp(): Boolean = true
    override fun showSnackBar(s: String) {
        if (view != null) Snackbar.make(view!!, s, Snackbar.LENGTH_SHORT).show()
    }


}

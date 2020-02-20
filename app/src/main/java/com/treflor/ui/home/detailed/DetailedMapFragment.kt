package com.treflor.ui.home.detailed

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.OnMapReadyCallback
import com.google.android.libraries.maps.model.*
import com.google.maps.android.PolyUtil
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

import com.treflor.R
import com.treflor.internal.ui.base.TreflorScopedFragment
import kotlinx.android.synthetic.main.journey_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

class DetailedMapFragment : TreflorScopedFragment(), OnMapReadyCallback, KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ((String) -> DetailedMapViewModelFactory) by factory()
    private lateinit var navController: NavController
    private lateinit var viewModel: DetailedMapViewModel
    private lateinit var id: String
    private var myPositionMarker: Marker? = null
    private var endLocationJourneyMarker: Marker? = null
    private var startLocationJourneyMarker: Marker? = null
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
        return inflater.inflate(R.layout.detailed_map_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailedMapViewModel::class.java)
        checkPermissions(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val safeArgs = arguments?.let { DetailedMapFragmentArgs.fromBundle(it) }
        id = safeArgs!!.id
        viewModel =
            ViewModelProviders.of(this, viewModelFactory(safeArgs.id))
                .get(DetailedMapViewModel::class.java)
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

        journey_map.onCreate(savedInstanceState)
        journey_map.getMapAsync(this@DetailedMapFragment)

        viewModel.location.observe(this@DetailedMapFragment, Observer {
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

        viewModel.journey.await().observe(this@DetailedMapFragment, Observer {
            if (it == null) return@Observer


            routePolyline =
                googleMap?.addPolyline(PolylineOptions().addAll(it.direction?.decodedPoints()))
            googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    it.direction?.getLatLngBounds(),
                    30
                )
            )
            trackedPolyline = googleMap?.addPolyline(
                PolylineOptions()
                    .addAll(PolyUtil.decode(it.trackedLocations))
                    .color(Color.GREEN)
                    .zIndex(2f)
            )

            val icLandmarkPinB = BitmapFactory.decodeResource(resources, R.drawable.landmark_pin)
            val icLandmarkPinSmall = Bitmap.createScaledBitmap(icLandmarkPinB, 70, 70, false)

            it.landmarks?.map { landmark ->
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
    }
}

package com.treflor.ui.journey

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.OnMapReadyCallback
import com.google.android.libraries.maps.model.BitmapDescriptorFactory
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.Marker
import com.google.android.libraries.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.treflor.R
import kotlinx.android.synthetic.main.journey_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class JourneyFragment : Fragment(), OnMapReadyCallback, KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: JourneyViewModelFactory by instance()
    private lateinit var viewModel: JourneyViewModel
    private var myPositionMarker: Marker? = null
    private var googleMap: GoogleMap? = null

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

    private fun checkPermissions(savedInstanceState: Bundle?) {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    bindUI(savedInstanceState)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    journey_map.visibility = View.GONE
                    txt_permission.visibility = View.VISIBLE
                }
            })
            .check()
    }

    private fun bindUI(savedInstanceState: Bundle?) {
        journey_map.onCreate(savedInstanceState)
        journey_map.getMapAsync(this)
        viewModel.location.observe(this, Observer {
            if (myPositionMarker == null) {

                val b = BitmapFactory.decodeResource(resources, R.drawable.ic_hiking);
                val smallMarker = Bitmap.createScaledBitmap(b, 70, 70, false)
                myPositionMarker = googleMap?.addMarker(
                    MarkerOptions()
                        .title("My location")
                        .snippet("I am here")
                        .position(LatLng(it.latitude, it.longitude))
                        .icon(
                            BitmapDescriptorFactory.fromBitmap(smallMarker)
                        )
                )
                googleMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            it.latitude,
                            it.longitude
                        ), 15f
                    )
                )
            } else {
                myPositionMarker?.position = LatLng(it.latitude, it.longitude)
            }
        })
    }


    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        map?.uiSettings?.isMapToolbarEnabled = false
    }

}

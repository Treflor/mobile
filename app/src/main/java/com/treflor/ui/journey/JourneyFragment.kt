package com.treflor.ui.journey

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.OnMapReadyCallback
import com.google.android.libraries.maps.model.LatLng
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
            Log.e("Fucking location","::::::::::: ${it.longitude} ::::::::::: ${it.longitude}:::::")
        })
    }


    override fun onMapReady(map: GoogleMap?) {
        map?.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 1.0))
                .title("Marker")
        )
    }

}

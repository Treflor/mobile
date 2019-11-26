package com.treflor.ui.journey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.OnMapReadyCallback
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.treflor.R
import kotlinx.android.synthetic.main.journey_fragment.*


class JourneyFragment : Fragment(), OnMapReadyCallback {


    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private lateinit var viewModel: JourneyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.journey_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(JourneyViewModel::class.java)
        journey_map.onCreate(savedInstanceState)
        journey_map.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap?) {
        map?.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 1.0))
                .title("Marker")
        )
    }

    override fun onResume() {
        super.onResume()
        journey_map.onResume()
    }

    override fun onStart() {
        super.onStart()
        journey_map.onStart()
    }

    override fun onStop() {
        super.onStop()
        journey_map.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        journey_map.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        journey_map.onLowMemory()
    }

}

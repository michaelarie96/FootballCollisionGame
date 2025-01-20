package com.example.hw1_208388124.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hw1_208388124.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textview.MaterialTextView

class ScoreMapFragment : Fragment() {

    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_score_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeMap()
    }

    private fun initializeMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map
        }
    }

    fun showLocation(latitude: Double, longitude: Double) {
        googleMap?.let { map ->
            val location = LatLng(latitude, longitude)

            map.clear()

            map.addMarker(
                MarkerOptions()
                .position(location)
                .title("Score Location"))

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }
}
package com.diontambz.storyapp.view.maps

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.diontambz.storyapp.R
import com.diontambz.storyapp.data.Result
import com.diontambz.storyapp.data.model.Story
import com.diontambz.storyapp.databinding.ActivityMapsBinding
import com.diontambz.storyapp.util.ViewModelFactory
import com.diontambz.storyapp.view.add_story.DetailStoryActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel
    private val boundBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mapsViewModel = ViewModelProvider(this, factory)[MapsViewModel::class.java]
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setupStory()
        getMyLocation()
        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(ContentValues.TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(ContentValues.TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun setupStory() {
        mapsViewModel.getUser().observe(this) { user ->
            val token = "Bearer " + user.token
            mapsViewModel.getStoryLocation(token).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> showMarker(result.data.listStory)
                    is Result.Error -> Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showMarker(data: List<Story>) {
        data.forEach {
            val latLng = LatLng(it.lat, it.lon)
            val marker = mMap.addMarker(
                MarkerOptions().position(latLng).title(getString(R.string.story_from) + it.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    .alpha(0.7f).snippet(it.description)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            boundBuilder.include(latLng)
            marker?.tag = it
            mMap.setOnInfoWindowClickListener {
                val intent = Intent(this, DetailStoryActivity::class.java).apply {
                    putExtra(DetailStoryActivity.EXTRA_DETAIL, it.tag as Story)
                }
                startActivity(intent)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}
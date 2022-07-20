package com.greentea.storyapp2.ui

import android.content.ContentValues
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.greentea.storyapp2.R
import com.greentea.storyapp2.databinding.ActivityMapsBinding
import com.greentea.storyapp2.services.models.repository.StoryRepository
import com.greentea.storyapp2.utils.Constants
import com.greentea.storyapp2.viewmodel.StoryViewModel
import com.greentea.storyapp2.viewmodel.factory.StoryViewModelProviderFactory
import com.greentea.storyapp2.viewmodel.preferences.UserPreference

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val tag1 = "MapsActivity"
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //CREATE CONNECTION ->
        val storyViewModelFactory = StoryViewModelProviderFactory(StoryRepository())
        storyViewModel = ViewModelProvider(
            this, storyViewModelFactory
        )[StoryViewModel::class.java]

        userPreference = UserPreference(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //ADDING CONTROL MAP
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val tokenFromPreferences = userPreference.getDataLogin(Constants.TOKEN)
        val realToken = "Bearer $tokenFromPreferences"
        storyViewModel.getListViaMap(realToken)

        observerMapUser()
        setMapStyle()
    }

    private fun observerMapUser() {
        storyViewModel.listMapUser.observe(this){ response ->
            if(response.isSuccessful){
                Log.i(tag1, "checking for the response: ${response.body()?.message}")

                val bounds = LatLngBounds.Builder()
                for(data in response.body()?.listStory!!){
                    val latLng = LatLng(data.lat.toDouble(), data.lon.toDouble())
                    bounds.include(latLng)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(data.name)
                            .snippet(data.description))
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 800, 800, 0))

            } else{
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //SETTING MAP STYLE
    private fun setMapStyle() {
        try{
            val success =
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success){
                Log.e(ContentValues.TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException){
            Log.e(ContentValues.TAG, "Can't find style. Error: ", exception)
        }
    }

    //SHOWING THE MENU
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    //HANDLE THE ACTION WHEN EACH MENU IS BEING CLICKED
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
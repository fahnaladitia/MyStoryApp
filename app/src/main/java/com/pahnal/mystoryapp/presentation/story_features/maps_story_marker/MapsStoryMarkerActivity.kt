package com.pahnal.mystoryapp.presentation.story_features.maps_story_marker

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.CancelableCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pahnal.mystoryapp.R
import com.pahnal.mystoryapp.databinding.ActivityMapsStoryMarkerBinding
import com.pahnal.mystoryapp.databinding.MainToolbarBinding
import com.pahnal.mystoryapp.utils.ViewModelFactory
import com.pahnal.mystoryapp.utils.goBack


class MapsStoryMarkerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsStoryMarkerBinding
    private var toolbar: MainToolbarBinding? = null
    private var boundsBuilder = LatLngBounds.Builder()
    private val viewModel by lazy {
        val factory = ViewModelFactory.getInstance(application)
        ViewModelProvider(this, factory)[MapsStoryMarkerViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsStoryMarkerBinding.inflate(layoutInflater)
        toolbar = binding.toolbarLayout
        setContentView(binding.root)
        setupUI()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupUI() {
        setupToolbar()
    }


    private fun setupToolbar() {
        toolbar?.root?.title = getString(R.string.story_mark)
        setSupportActionBar(toolbar?.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> goBack()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMarkerFromListStories()
        setMarkerClick()
    }

    private fun setMarkerClick() {
        mMap.setOnMarkerClickListener {
            showDialogMarkerAndZoomIn(
                it.title,
                it.snippet,
                it.position,
            )
            true
        }
    }

    private fun showDialogMarkerAndZoomIn(
        title: String?,
        description: String?,
        position: LatLng,
    ) {

        val dialog = MaterialAlertDialogBuilder(
            this,
            R.style.Theme_MyStoryApp_AlertDialogConfirmation,
        ).setTitle(title).setMessage(description)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15f),
            1000,
            object : CancelableCallback {
                override fun onCancel() = Unit
                override fun onFinish() {
                    dialog.show()
                }
            })

    }

    private fun setMarkerFromListStories() {
        viewModel.getAllStories().observe(this) { stories ->
            stories?.forEach { story ->
                story.latLng()?.let { latLng ->
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(story.name)
                            .snippet(story.description)
                    )
                    boundsBuilder.include(latLng)
                }
            }
            if (stories?.isEmpty() == true) return@observe
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 64))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        toolbar = null
    }

    companion object {
        const val EXTRA_STORY_LIST = "extraStory"
    }
}
package com.ksa.agenceCompany.ui.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.base.BaseActivity
import com.ksa.agenceCompany.databinding.ActivityMapsBinding
import java.io.IOException
import java.util.*


class MapsActivity : BaseActivity<ActivityMapsBinding>(), OnMapClickListener, OnMapReadyCallback {

    override fun getLayoutId(): Int = R.layout.activity_maps


    private lateinit var task: Task<LocationSettingsResponse>
    private lateinit var clientLocationServices: SettingsClient
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private val REQUEST_CHECK_SETTINGS = 10001
    var fullADDRESS: String? = null
    var CITY: kotlin.String? = null
    var LATITUDE = 1.55555
    var LONGITUDE: Double = 1.55566
    var geocoderMaxResults = 1

    //    private GoogleMap mMap;
    lateinit var client: FusedLocationProviderClient

    //    private SupportMapFragment mapFragment;
    private var manager: LocationManager? = null
    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding = ActivityMapsBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        manager = getSystemService(LOCATION_SERVICE) as LocationManager
        //Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this)
        //Initialize the SDK Autocomplete
        //val apiKey = API_KEY
        val apiKey = "AIzaSyBCXn9fjD1eSTVPsnx6yK5-WRgT7ePg4x0"
        Places.initialize(applicationContext, apiKey)

        val locationRequest = LocationRequest.create()
        locationRequest.interval = 3000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        clientLocationServices = LocationServices.getSettingsClient(this)
        task = clientLocationServices.checkLocationSettings(builder.build())

        task.addOnCompleteListener { task1: Task<LocationSettingsResponse?> ->
            try {
                val response = task1.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.
                getCurrentLocation()
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                         // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                this@MapsActivity, REQUEST_CHECK_SETTINGS
                            )
                        } catch (e: SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
        onClick()
        makeStatusbarTransparent()
    }


    private fun getCurrentLocation() {

        //Initialize task location
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val task = client.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(onTokenCanceledListener: OnTokenCanceledListener): CancellationToken {
                    return this
                }

                override fun isCancellationRequested(): Boolean {
                    return false
                }
            })
        task.addOnSuccessListener { location: Location ->
            //when success

            //when success
            if (location != null) {
                //Sny map
                onMapReady(location)
            }
        }
        client.lastLocation.addOnSuccessListener(
            this
        ) { location -> // Got last known location. In some rare situations this can be null.
            location?.let {
                onMapReady(it)
            }
        }
    }


    fun getAddressFromLatLng(lat: Double, lng: Double, geocoderMaxResults: Int): String? {
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(lat, lng, geocoderMaxResults)
            val address = addresses!![0]
            fullADDRESS = address.getAddressLine(0) +
                    address.countryName +
                    address.adminArea
            CITY = address.adminArea
            fullADDRESS

        } catch (e: IOException) {
            e.printStackTrace()
            //            binding.btnAddAddress.setEnabled(false);
            "null"
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val place: com.google.android.libraries.places.api.model.Place =
                    Autocomplete.getPlaceFromIntent(data)
                val TAG = "Info"
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId())
                LATITUDE = Objects.requireNonNull(place.getLatLng()).latitude
                LONGITUDE = place.latLng.longitude

                mMap.addMarker(
                    MarkerOptions().position(place.getLatLng())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_outline))
                )
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 17f))
                mViewDataBinding.tvAutocomplete.setText(
                    getAddressFromLatLng(
                        LATITUDE, LONGITUDE, geocoderMaxResults
                    )
                )
                mViewDataBinding.tvResultAddress.text = getAddressFromLatLng(
                    LATITUDE, LONGITUDE, geocoderMaxResults
                )

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
//                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("dialog", "111")
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("dialog", "111")
            }
            return
        }
//        val states = LocationSettingsStates.fromIntent(data!!)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            when (resultCode) {
                RESULT_OK -> {
                    Log.d("test", "REQUEST_CHECK_SETTINGS")
                    // All required changes were successfully made
                    getCurrentLocation()
                }

                RESULT_CANCELED -> {
                    // The user was asked to change settings, but chose not to
                    Log.d("test", "RESULT_CANCELED")
                    finish()
                }

                else -> {}
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onMapReady(location: Location) {
        if (location != null) {

            // Initialize lat lng
            val sydney = LatLng(location.latitude, location.longitude)
            //create Marker option
            val options = MarkerOptions().position(sydney)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_outline))
            LATITUDE = location.latitude
            LONGITUDE = location.longitude
            //zoom map
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17f))
            //add marker on map
            mMap.addMarker(options)
            getAddressFromLatLng(LATITUDE, LONGITUDE, geocoderMaxResults)

            mViewDataBinding.tvResultAddress.text = fullADDRESS
//            mMap.setOnMapClickListener(MapsActivity.this);
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener(this@MapsActivity)

    }

    override fun onMapClick(latLng: LatLng) {
        // Creating a marker
        val markerOptions = MarkerOptions()
        // Setting the position for the marker
        markerOptions.position(latLng)
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_outline))

        //        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.mar));
        LATITUDE = latLng.latitude
        LONGITUDE = latLng.longitude
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, geocoderMaxResults)
            val address = addresses!![0]
            fullADDRESS = address.getAddressLine(0) + address.adminArea + address.subAdminArea
            CITY = address.adminArea
            markerOptions.title(address.getAddressLine(0) + "")
            mViewDataBinding.tvResultAddress.text = fullADDRESS
            mViewDataBinding.tvAutocomplete.text = ""

        } catch (e: Exception) {
            //e.printStackTrace();
        }
        // Clears the previously touched position
        mMap.clear()
        // Animating to the touched position
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        // Placing a marker on the touched position
        mMap.addMarker(markerOptions)

    }


    private fun onClick() {
        mViewDataBinding.btnPickLocation.setOnClickListener { view ->
            val intent = Intent()
            intent.putExtra("ADDRESS", fullADDRESS)
            intent.putExtra("CITY", CITY)
            intent.putExtra("LAT", LATITUDE)
            intent.putExtra("LON", LONGITUDE)
            setResult(RESULT_OK, intent)
            finish()
        }
        mViewDataBinding.tvAutocomplete.setOnClickListener { view ->
            if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else {
                // getCurrentLocation();
                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                val fields = Arrays.asList(
                    Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME
                )


                // Start the autocomplete intent.
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(this@MapsActivity)
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
            }
        }

    }


    private fun makeStatusbarTransparent() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.your_GPS_seems_to_be_disabled_do_you_want_to_enable_it))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { dialog, id ->
                startActivityForResult(
                    Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                    REQUEST_CHECK_SETTINGS
                )
            }
            .setNegativeButton(getString(R.string.no)) { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

}

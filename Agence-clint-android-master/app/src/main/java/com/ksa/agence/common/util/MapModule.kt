//package com.sa.nafhaseha.common.util
//
//import android.Manifest
//
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Looper
//import android.app.Activity
//import android.content.Context
//import android.location.Geocoder
//import android.location.Location
//import android.widget.Toast
//import android.net.Uri
//import android.util.Log
//import androidx.core.app.ActivityCompat
//import com.airbnb.lottie.model.Marker
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.MapFragment
//import com.google.android.gms.maps.model.*
//
//
//import java.io.IOException
//import java.util.*
//
//class MapModule : GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener {
//    var context: Context
//    var map: GoogleMap? = null
//    var mFusedLocationProviderClient: FusedLocationProviderClient
//    var mapFragment: MapFragment? = null
//    var markers: ArrayList<Marker>
//    private var location: Location? = null
//    var latLng: LatLng? = null
//    var locationCallback: LocationCallback? = null
//    var getLatLng: GetLatLng
//
//    //
//    //public void AddMultiMarker(Context context,final List<Estates> mapModules,String type) {
//    //    Marker marker;
//    //    startMove=false;
//    //    for (int i=0;i<mapModules.size();i++)
//    //    {
//    //        IconGenerator iconGen = new IconGenerator(context);
//    //        iconGen.setBackground(context.getResources().getDrawable(R.drawable.marker_blue));
//    //        iconGen.setTextAppearance(R.style.myStyleText);
//    //
//    //        marker= map.addMarker(new MarkerOptions().
//    //                position(new LatLng(Double.parseDouble(mapModules.get(i).getLat()),Double.parseDouble(mapModules.get(i).getLang())))
//    //
//    //                .icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(mapModules.get(i).getName()))));
//    //
//    //        markers.add(marker);
//    //
//    //    }
//    //
//    //    if (!markers.isEmpty()) {
//    //        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//    //        for (Marker markerrr : markers) {
//    //            builder.include(markerrr.getPosition());
//    //            //  onMarkerClick(markerrr);
//    //        }
//    //        LatLngBounds bounds = builder.build();
//    //        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,5,10,1);
//    //     //   map.setOnCameraIdleListener(this );
//    //
//    //        if (!type.equals("0")) {
//    //            map.moveCamera(cu);
//    //            map.animateCamera(CameraUpdateFactory.zoomTo(8));
//    //        }
//    //
//    //
//    //
//    //
//    //
//    //    }
//    //}
//    var startMove = false
//
//    constructor(context: Context, getLatLng: GetLatLng) {
//        this.context = context
//        //        this.map = map;
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
//        //CheckGPSDevice();
//        SetonMapReady()
//        markers = ArrayList()
//        this.getLatLng = getLatLng
//    }
//
//    constructor(context: Context, map: GoogleMap?, getLatLng: GetLatLng) {
//        this.context = context
//        this.map = map
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
//        // CheckGPSDevice();
//        SetonMapReady()
//        markers = ArrayList()
//        this.getLatLng = getLatLng
//    }
//
//
//    fun SetonMapReady() {
//        try {
////            map.setMyLocationEnabled(true);
////            map.getUiSettings().setMyLocationButtonEnabled(true);
//        } catch (e: SecurityException) {
//        }
//    }
//
//    fun ClickMapWindowInfo(googleMap: GoogleMap) {
//        googleMap.setOnInfoWindowClickListener { marker ->
//            val uri = String.format(
//                Locale.ENGLISH,
//                "geo:%f,%f",
//                marker.position.latitude,
//                marker.position.longitude
//            )
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
//            context.startActivity(intent)
//        }
//    }
//
//    private fun startLocationUpdates() {
//        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//        val locationRequest = LocationRequest.create()
//        locationRequest.interval = 10000
//        locationRequest.fastestInterval = 5000
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                if (locationResult == null) {
//                    Log.e("mainFrag", "locationCallBackNull")
//                    return
//                }
//                for (location in locationResult.locations) {
//                    // Update UI with location data
//                    // ...
//                    fusedLocationClient.removeLocationUpdates(locationCallback)
//                    Log.e("mainFrag", "locationCallBack")
//                }
//            }
//        }
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        fusedLocationClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            Looper.getMainLooper()
//        )
//    }
//
//    fun getLastLocation(): Location? {
//        Log.e("MainFrag", "call location.")
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            return null
//        }
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
//        mFusedLocationProviderClient.lastLocation.addOnSuccessListener((context as Activity)) { location ->
//            if (location != null) {
//                val newLatLng = LatLng(location.latitude, location.longitude)
//                latLng = newLatLng
//                Log.e("MainFrag", "Move Cam.")
//                getLatLng.LatLngLocation(newLatLng)
//                this@MapModule.location = location
//                if (map != null) {
//                    Log.e("MainFrag", "MAP Not Null Cam.")
//                    map!!.moveCamera(CameraUpdateFactory.newLatLng(newLatLng))
//                    map!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
//                    map!!.setOnCameraMoveListener { Log.e("MainFrag", "Move Cam.") }
//                }
//            } else {
//                startLocationUpdates()
//                getLastLocation()
//                //          Log.e("MainFrag", "null");
//            }
//        }
//       map!!.setMyLocationEnabled(true)
//        val locationCallback: LocationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                if (locationResult == null) {
//                    return
//                }
//                for (location in locationResult.locations) {
//                    // Update UI with location data
//                    // ...
//                    Toast.makeText(context, location.latitude.toString(), Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//        return location
//    }
///*
//    private fun stylePolygon(polygon: Polygon) {
//        var type = ""
//        // Get the data object stored with the polygon.
//        if (polygon.tag != null) {
//            type = polygon.tag.toString()
//        }
//        var pattern: List<PatternItem?>? = null
//        var strokeColor = context.resources.getColor(R.color.colorAccent)
//        var fillColor = context.resources.getColor(R.color.colorPrimary)
//        when (type) {
//            "A" -> {
//                // Apply a stroke pattern to render a dashed line, and define colors.
//                pattern = PATTERN_POLYGON_ALPHA
//                strokeColor = context.resources.getColor(R.color.colorAccent)
//                fillColor = context.resources.getColor(R.color.colorPrimary)
//            }
//        }
//        polygon.setStrokePattern(pattern)
//        polygon.strokeWidth = POLYGON_STROKE_WIDTH_PX.toFloat()
//        polygon.strokeColor = strokeColor
//        polygon.fillColor = fillColor
//    }
//*/
//    override fun onMarkerClick(marker: Marker): Boolean {
//        Toast.makeText(context, marker.title, Toast.LENGTH_SHORT).show()
//        return false
//    }
//
//    override fun onCameraIdle() {
//
////        latLng = new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
////    getLatLng.LatLngLocation(latLng);
//    }
//
//    fun getAddress(latLng: LatLng?, context: Context?): String? {
//        if (latLng != null) {
//            var address = ""
//            try {
//                val geocoder: Geocoder
//                val addresses: List<Address>
//                geocoder = Geocoder(
//                    context, Locale(
////                        Utilities.getCachedString(
////                            context!!, LANG, LANG
////                        )
//                    "ar"
//                    )
//                )
//                addresses = geocoder.getFromLocation(
//                    latLng.latitude,
//                    latLng.longitude,
//                    1
//                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                if (addresses.size > 0)
//                    address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                // String city = addresses.get(0).getLocality();
//                //  String city2 = addresses.get(0).get();
////                String state = addresses.get(0).getAdminArea();
////                String country = addresses.get(0).getCountryName();
////                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
//             Log.e("addddddd", address);
////                Log.e("addddddd", city);
//                // Log.e("addddddd", city2);
//                //   Log.e("addddddd", state);
//                //  Log.e("addddddd", country);
//                //  Log.e("addddddd", knownName);
//                return address
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        return null
//    }
//
//    companion object {
//        //    public void addMarker(double lat,double lng,String title)
//        //    {
//        //        map.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(title));
//        //    }
//        //
//        //    public void addMarker(double lat,double lng,String title,int resid)
//        //    {
//        //        map.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(title).icon(BitmapDescriptorFactory.fromResource(resid)));
//        //    }
//        //
//        //    public void Create_Polyline(ArrayList<MapModel>mapModules,String tag)
//        //    {
//        //        List<LatLng> latLngs=new ArrayList<>();
//        //        for (int x=0;x<mapModules.size();x++) {
//        //            latLngs.add(new LatLng(mapModules.get(x).getLat(),mapModules.get(x).getLng()));
//        //        }
//        //        Polyline polyline=map.addPolyline(new PolylineOptions().clickable(true).addAll(latLngs));
//        //
//        //        polyline.setTag(tag);
//        //        stylePolyline(polyline);
//        //    }
//        //    private void stylePolyline(Polyline polyline) {
//        //        String type = "";
//        //        // Get the data object stored with the polyline.
//        //        if (polyline.getTag() != null) {
//        //            type = polyline.getTag().toString();
//        //        }
//        //
//        //        switch (type) {
//        //            // If no type is given, allow the API to use the default.
//        //            case "A":
//        ////                // Use a custom bitmap as the cap at the start of the line.
//        ////                polyline.setStartCap(
//        ////                        new CustomCap(
//        ////                                BitmapDescriptorFactory.fromResource(R.drawable.pin), 10));
//        //                break;
//        //            case "B":
//        //                // Use a round cap at the start of the line.
//        //                polyline.setStartCap(new RoundCap());
//        //                break;
//        //        }
//        //
//        //        polyline.setEndCap(new RoundCap());
//        //        polyline.setWidth(14);
//        //        polyline.setColor(context.getResources().getColor(R.color.colorPrimaryDark));
//        //        polyline.setJointType(JointType.ROUND);
//        //    }
//        //    public void Create_polygon(ArrayList<MapModel>mapModules,String tag)
//        //    {
//        //
//        //        List<LatLng> latLngs=new ArrayList<>();
//        //        for (int x=0;x<mapModules.size();x++) {
//        //            latLngs.add(new LatLng(mapModules.get(x).getLat(),mapModules.get(x).getLng()));
//        //
//        //        }
//        //        Polygon polyline =map.addPolygon(new PolygonOptions().clickable(true).addAll(latLngs));
//        //        polyline.setTag(tag);
//        //        stylePolygon(polyline);
//        //        }
//        private const val POLYGON_STROKE_WIDTH_PX = 8
//        private const val PATTERN_DASH_LENGTH_PX = 20
//        private const val PATTERN_GAP_LENGTH_PX = 20
//        private val DOT: PatternItem = Dot()
//        private val DASH: PatternItem = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
//        private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())
//
//        // Create a stroke pattern of a gap followed by a dash.
//        private val PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH)
//
//        // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
//        private val PATTERN_POLYGON_BETA = Arrays.asList(DOT, GAP, DASH, GAP)
//    }
//}
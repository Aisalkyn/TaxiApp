package com.example.aisa.taxiapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.aisa.taxiapp.R
import com.example.aisa.taxiapp.ui.map.MapActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_main.*



@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(),   GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    internal lateinit var mLocationRequest: LocationRequest

    private val LOCATION_PERMISSION = 100
    private var mGoogleApiClient: GoogleApiClient? = null
    private var longitude: Double? = null
    private var latitude: Double? = null
    private var perm: Boolean = false
    private var mMap: GoogleMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

    }
    private fun init(){
        initMyLoc()
        initBtn()

    }
    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {

        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient)
        if (mLastLocation != null) {
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
            Log.d("mm>>><<<", latitude.toString() + " " + longitude)

        }
    }


    private fun initMyLoc() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
        }
        if (mGoogleApiClient != null) {
            this.mGoogleApiClient!!.connect()
        }
    }

    private fun requestLocationPermission() {
        mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION)
            perm = true
        } else {
           // mMap!!.isMyLocationEnabled = true
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    private fun initBtn(){
        button_find.setOnClickListener {
            requestLocationPermission()
            Log.d("_-____________-_!", latitude.toString() + " " + longitude)
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("lat", latitude)
            intent.putExtra("lon", longitude)
            startActivity(intent)
        }
    }
}

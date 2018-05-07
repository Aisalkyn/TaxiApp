package com.example.aisa.taxiapp.ui

import android.Manifest
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
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(),   GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    internal lateinit var mLocationRequest: LocationRequest

    private val LOCATION_PERMISSION = 100
    var mGoogleApiClient: GoogleApiClient? = null
    var longitude: Double? = null
    var latitude: Double? = null
    var perm: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

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
            //mMap!!.isMyLocationEnabled = true
        }
    }
    override fun onConnected(p0: Bundle?) {

        try {

            if (perm) {
                val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude()
                    longitude = mLastLocation.getLongitude()
                    Log.d("mm>>><<<", latitude.toString() + " " + longitude)

                }
            }
        } catch (e: SecurityException) {

        }

    }


    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }
    private fun init(){
        requestLocationPermission()

        initMyLoc()
        button_find.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("lat", latitude!!)
            intent.putExtra("lon", longitude!!)
            startActivity(intent)
        }
    }
}

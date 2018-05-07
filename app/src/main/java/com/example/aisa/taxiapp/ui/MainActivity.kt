package com.example.aisa.taxiapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.aisa.taxiapp.R
import com.example.aisa.taxiapp.ui.map.MapActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(),   GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    var mGoogleApiClient: GoogleApiClient? = null
    var longitude: Double? = null
    var latitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

    }
    private fun initMyLoc(){

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


    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
           latitude = mLastLocation.getLatitude()
            longitude = mLastLocation.getLongitude()
            Log.d("mm>>><<<", latitude.toString() + " " + longitude)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }
    private fun init(){
        initMyLoc()
        button_find.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("lat", latitude!!)
            intent.putExtra("lon", longitude!!)
            startActivity(intent)
        }
    }
}

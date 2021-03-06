package com.example.aisa.taxiapp.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.aisa.taxiapp.R
import com.example.aisa.taxiapp.StartApplication
import com.example.aisa.taxiapp.model.Contact
import com.example.aisa.taxiapp.model.MainModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.item_of_company.view.*
import android.content.Intent
import android.net.Uri
import com.example.aisa.taxiapp.model.Company


class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        MapContract.View {


    private lateinit var presenter: MapPresenter
    internal lateinit var mLocationRequest: LocationRequest
    private var mMap: GoogleMap? = null
    private val LOCATION_PERMISSION = 100
    private var builder: LatLngBounds.Builder? = null
    private var company: Company? = null
    private var infoWindoAdapter: CustomInfoWindowAdapter? = null
    private var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_map)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        infoWindoAdapter = CustomInfoWindowAdapter(this)

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.setInfoWindowAdapter(infoWindoAdapter)
        if (checkLocationPermission()) {
            init()
        }
        mMap?.setOnInfoWindowClickListener(this)

    }

    override fun onConnected(p0: Bundle?) {
        redirect()
    }

    private fun init() {
        Log.i("______________", "msg123")

        val mLastLocation = onLocation()

        val app: StartApplication = applicationContext as StartApplication
        presenter = MapPresenter(app.service, this, this)
        if (mLastLocation?.latitude != null)
            presenter.loadMap(mLastLocation.latitude, mLastLocation.longitude)

    }
    override fun onInfoWindowClick(p0: Marker?) {
        val r = p0?.tag as Company
        val m = r.contacts!![1].contact

        val callIntent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", m, null) )
        startActivity(callIntent)

    }

    private fun checkLocationPermission(): Boolean {
        mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION)
            return false
        }

        mMap?.isMyLocationEnabled = true
        return true
    }

    private fun redirect() {
        val mLastLocation = onLocation()
        Log.i("______________", "${mLastLocation?.latitude} ${mLastLocation?.longitude}")
        if (mLastLocation != null) {
            moveCameraDirection()
            Toast.makeText(this, "lat" + mLastLocation.latitude + "long" + mLastLocation.longitude, Toast.LENGTH_LONG).show()
        }
    }

    private fun onLocation(): LatLng? {
        if (checkLocationPermission()) {
            // instantiate the location manager, note you will need to request permissions in your manifest
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            // get the last know location from your location manager.
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            // now get the lat/lon from the location and do something with it.
            return LatLng(location.latitude, location.longitude)

        }
        return null
    }

    override fun onMapSuccess(model: MainModel) {

        for (j in 0 until (model.companies!!.size))
            for (i in 0 until (model.companies!![j].drivers!!.size)) {
                val cars = LatLng(model.companies!![j].drivers!![i].lat!!, model.companies!![j].drivers!![i].lon!!)
                marker = mMap?.addMarker(MarkerOptions().position(cars)!!
                        .flat(false)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.nnamb)))

                mMap?.setInfoWindowAdapter(infoWindoAdapter)
                marker?.tag = model.companies!![j]

                Log.i("onMap_____________FFSDF", marker?.tag.toString())


            }

        moveCameraDirection()
    }



    override fun onMapFail(message: String) {
        Log.d("errrrr", message)
    }

    private fun moveCameraDirection() {
        if (mMap != null && builder != null) {
            try {
                val updatePosition = CameraUpdateFactory.newLatLngBounds(builder?.build(), 500, 500, 5)
                mMap?.animateCamera(updatePosition)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if (builder == null && mMap != null) {
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(42.88200, 74.58274), 10f))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            LOCATION_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The permission request was granted, we set up the marker
                    redirect()
                    init()
                } else {
                    // The permission request was denied, we make the user aware of why the location is not shown
                    Toast.makeText(this, "Since the permission wasn't granted we can't show the location", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }
}
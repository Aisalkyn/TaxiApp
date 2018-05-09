package com.example.aisa.taxiapp.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.example.aisa.taxiapp.R
import com.example.aisa.taxiapp.StartApplication
import com.example.aisa.taxiapp.model.MainModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback,
        GoogleMap.InfoWindowAdapter, /*GoogleMap.OnMarkerClickListener*/
        MapContract.View {

    var longitude: Double? = null
    var latitude: Double? = null
    private lateinit var presenter: MapPresenter
    internal lateinit var mLocationRequest: LocationRequest
    private var mMap: GoogleMap? = null
    private val LOCATION_PERMISSION = 100
    private var builder: LatLngBounds.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_map)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.uiSettings?.isZoomControlsEnabled = true
        mMap?.setInfoWindowAdapter(this)
        requestLocationPermission()

        setBranches()
        init()
    }

    private fun init() {
        getData()
        val app: StartApplication = applicationContext as StartApplication
        presenter = MapPresenter(app.service, this, this)
        if(latitude != null && longitude != null)
            presenter.loadMap(this.latitude!!, this.longitude!!)
    }


    fun setBranches() {
        builder = LatLngBounds.builder()
    }

    private fun setMarker(latLng: LatLng) {
        val markerOptions = MarkerOptions()
                .anchor(0.5f, 0.5f) // Anchors the marker on the bottom left
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location))
        mMap.let {
            val m = it!!.addMarker(markerOptions)
        builder?.include(m?.position)
        }

    }




    private fun getData() {
        val intent = intent
        latitude = intent.getDoubleExtra("lat", 0.0)
        longitude = intent.getDoubleExtra("lon", 0.0)

    }

    /*  private fun fillData() {
          if (branches != null && mMap != null) {
              mMap?.clear()
              for (i in branches!!.indices) {
                  setMarker(LatLng(branches!!.get(i).latitude!!,
                          branches!!.get(i).longitude!!),
                          branches!!.get(i).address!!
                  )
              }
          }
      }*/

    private fun requestLocationPermission() {
        mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION)
        } else {
            mMap?.isMyLocationEnabled = true
        }
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


    override fun getInfoContents(marker: Marker?): View? {

        return null
    }

    override fun getInfoWindow(marker: Marker?): View? {
        return null

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (true) {
            val alertadd = AlertDialog.Builder(this)
            val factory = LayoutInflater.from(this)
            val view = factory.inflate(R.layout.dialog_layout, null)
            alertadd.setView(view)
            alertadd.show()
            return true
        }
        return false
    }

    override fun onMapSuccess(model: MainModel) {
        for (j in 0..model.companies!!.size - 1)
            for (i in 0..model.companies!![j].drivers!!.size - 1) {
                setMarker(LatLng(model.companies!![j].drivers!![i].lat!!, model.companies!![j].drivers!![i].lon!!))
            }
        moveCameraDirection()
    }

    override fun onMapFail(message: String) {
        Log.d("errrrr", message)
    }
}
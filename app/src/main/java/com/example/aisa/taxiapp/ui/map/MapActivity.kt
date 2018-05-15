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
import android.widget.Toast
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
        GoogleMap.InfoWindowAdapter,
        MapContract.View {


    var longitude: Double? = null
    var latitude: Double? = null
    private lateinit var presenter: MapPresenter
    internal lateinit var mLocationRequest: LocationRequest
    private var mMap: GoogleMap? = null
    private val LOCATION_PERMISSION = 100
    private var builder: LatLngBounds.Builder? = null
    private var taxiInfo: MainModel? = null
    private var builderAl: AlertDialog.Builder? = null
    private var alertDialog: AlertDialog? = null

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

        setBranches()
        if (checkLocationPermission()) {
            init()
        }


    }

    private fun init() {
        getData()
        val app: StartApplication = applicationContext as StartApplication
        presenter = MapPresenter(app.service, this, this)
        if (latitude != null && longitude != null)
            presenter.loadMap(this.latitude!!, this.longitude!!)

    }


    fun setBranches() {
        builder = LatLngBounds.builder()
    }

//    private fun setMarkerSmsTaxi(latLng: LatLng, title: String, contact: Contact, image: String) {
//        val markerOptions = MarkerOptions()
//                .anchor(0.5f, 0.5f) // Anchors the marker on the bottom left
//                .position(latLng)
//                .title(title)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sms))
//        mMap.let {
//            val m = it!!.addMarker(markerOptions)
//            builder?.include(m?.position)
//        }
//        mMap!!.setOnMarkerClickListener { onCl(contact, title, image) }
//
//    }
//
//
//    private fun setMarkerNamba(latLng: LatLng, title: String, contact: Contact, image: String) {
//        val markerOptions = MarkerOptions()
//                .anchor(0.5f, 0.5f) // Anchors the marker on the bottom left
//                .position(latLng)
//                .title(title)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.namba))
//
//        mMap.let {
//            val m = it!!.addMarker(markerOptions)
//            builder?.include(m?.position)
//        }
//        mMap!!.setOnMarkerClickListener { onCl(contact, title, image) }
//

    //}
    private fun setMarker(latLng: LatLng) {
        val markerOptions = MarkerOptions()
                .anchor(0.5f, 0.5f) // Anchors the marker on the bottom left
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.namba))

        mMap.let {
            val m = it!!.addMarker(markerOptions)
            builder?.include(m?.position)
        }
        mMap!!.setOnMarkerClickListener { onCl() }


    }

    fun onCl(/*model: Contact, name: String, image: String*/): Boolean {
        var view = LayoutInflater.from(this).inflate(R.layout.item_taxi, null, false)
        //view.phone.text = model.contact
        //view.name.text = name
        //view.company_logo.set(image)
        builderAl = AlertDialog.Builder(this).setView(view)
        alertDialog = builderAl!!.create()
        alertDialog!!.show()

        return true

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


        override fun onMapSuccess(model: MainModel) {
        for (j in 0 until model.companies!!.size)
            for (i in 0 until model.companies!![j].drivers!!.size) {
                setMarker(LatLng(model.companies!![j].drivers!![i].lat!!, model.companies!![j].drivers!![i].lon!!))
            }
        moveCameraDirection()
    }
//    override fun onMapSuccess(model: MainModel) {
//        for (i in 0 until model.companies!![0].drivers!!.size) {
//            setMarkerSmsTaxi(LatLng(model.companies!![0].drivers!![i].lat!!,
//                    model.companies!![0].drivers!![i].lon!!),
//                    model.companies!![0].name!!, model.companies!![0].contacts!![1],
//                    model.companies!![0].icon.toString()
//            )
//        }
//        for (i in 0 until model.companies!![1].drivers!!.size) {
//            setMarkerNamba(LatLng(model.companies!![1].drivers!![i].lat!!,
//                    model.companies!![1].drivers!![i].lon!!),
//                    model.companies!![1].name!!, model.companies!![1].contacts!![1], model.companies!![1].icon.toString()
//            )
//        }
//        moveCameraDirection()
//    }

    override fun onMapFail(message: String) {
        Log.d("errrrr", message)
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        // if accepted, init presenter again. init()
//
  //  }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            LOCATION_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The permission request was granted, we set up the marker
                    init()
                } else {
                    // The permission request was denied, we make the user aware of why the location is not shown
                    Toast.makeText(this, "Since the permission wasn't granted we can't show the location", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
}
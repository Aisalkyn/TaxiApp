package com.example.aisa.taxiapp.ui.map

import com.example.aisa.taxiapp.model.MainModel
import com.google.android.gms.maps.model.Marker

interface MapContract {
    interface View {
        fun onMapSuccess(model: MainModel)
        fun onMapFail(message: String)


        fun onMarkerClick(marker: Marker): Boolean
    }

    interface Presenter  {
        fun loadMap(lat: Double, lon: Double)

    }
}
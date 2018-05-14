package com.example.aisa.taxiapp.ui.map

import com.example.aisa.taxiapp.model.MainModel

interface MapContract {
    interface View {
        fun onMapSuccess(model: MainModel)
        fun onMapFail(message: String)



    }

    interface Presenter  {
        fun loadMap(lat: Double, lon: Double)

    }
}
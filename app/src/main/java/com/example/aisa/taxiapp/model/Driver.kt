package com.example.aisa.taxiapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName




class Driver {

    @SerializedName("lat")
    var lat: Double? = null
    @SerializedName("lon")
    var lon: Double? = null

}
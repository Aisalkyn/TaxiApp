package com.example.aisa.taxiapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Contact {

    @SerializedName("type")
    var type: String? = null
    @SerializedName("contact")
    var contact: String? = null

}

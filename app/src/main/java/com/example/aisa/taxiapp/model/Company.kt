package com.example.aisa.taxiapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by nestana on 06.05.2018.
 */
class Company {

    @SerializedName("name")
    var name: String? = null
    @SerializedName("icon")
    var icon: String? = null
    @SerializedName("contacts")
    var contacts: List<Contact>? = null
    @SerializedName("drivers")
    var drivers: List<Driver>? = null

}
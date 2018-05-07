package com.example.aisa.taxiapp.model

import com.google.gson.annotations.SerializedName


class MainModel {

    @SerializedName("success")
    var success: Boolean? = null
    @SerializedName("companies")
    var companies: List<Company>? = null

}

package com.example.nestana.myweather.utils

import android.graphics.ColorSpace
import com.example.aisa.taxiapp.model.MainModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ForumService {

    @GET("/nearest/{lat}/{lon}")
    fun getTaxiInfo(@Path("lat") lat: Double,
                           @Path("lon") lon: Double): Call<MainModel>


}
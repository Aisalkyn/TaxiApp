package com.example.aisa.taxiapp

import android.app.Application
import com.example.nestana.myweather.utils.ForumService
import com.example.nestana.myweather.utils.Network

class StartApplication : Application() {

    private val URL = "http://openfreecabs.org/"

    var service: ForumService? = null


    override fun onCreate() {
        super.onCreate()
        init()
    }

     private fun init(){
         service = Network.Companion.initService(URL)

     }
//http://openfreecabs.org//nearest/{lat}/{lon}
}
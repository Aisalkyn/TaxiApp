package com.example.aisa.taxiapp.ui.map

import android.content.Context
import android.widget.Toast
import com.example.aisa.taxiapp.model.MainModel
import com.example.nestana.myweather.utils.ForumService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapPresenter(var service: ForumService?,
                   var context: Context?, var view: MapContract.View?) : MapContract.Presenter {

    override fun loadMap(lat: Double, lon: Double) {
        service!!.getTaxiInfo(lat, lon).enqueue(object: Callback<MainModel> {
            override fun onResponse(call: Call<MainModel>, response: Response<MainModel>?) {
                if (response!!.isSuccessful) {
                    view!!.onMapSuccess(response.body()!!)
                }
                else{
                    view!!.onMapFail(response.message())

                }
            }

            override fun onFailure(call: Call<MainModel>?, t: Throwable?) {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()

            }
        })
    }

}
package com.example.aisa.taxiapp.ui.map

import android.annotation.SuppressLint
import com.google.android.gms.maps.model.Marker
import android.content.Context
import android.view.View
import com.example.aisa.taxiapp.R
import com.google.android.gms.maps.GoogleMap
import android.util.Log
import android.view.LayoutInflater
import com.example.aisa.taxiapp.model.Company
import com.example.aisa.taxiapp.model.MainModel
import kotlinx.android.synthetic.main.item_of_company.view.*


class CustomInfoWindowAdapter( var context: Context) : GoogleMap.InfoWindowAdapter {

    private lateinit var inflater: LayoutInflater
    var model: Company? = null

    @SuppressLint("InflateParams")
    override fun getInfoContents(marker: Marker): View? {

        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.item_of_company, null)

        model = marker.tag as Company?

        v.name.text = model!!.name
        v.phone.text = model!!.contacts!![0].contact
        v.address.text = model!!.contacts!![1].contact
        return v
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }
}
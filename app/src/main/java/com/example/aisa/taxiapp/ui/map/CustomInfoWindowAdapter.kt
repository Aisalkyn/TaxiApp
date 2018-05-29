package com.example.aisa.taxiapp.ui.map

import android.annotation.SuppressLint
import com.google.android.gms.maps.model.Marker
import android.content.Context
import android.view.View
import com.example.aisa.taxiapp.R
import com.google.android.gms.maps.GoogleMap
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.example.aisa.taxiapp.model.Company
import kotlinx.android.synthetic.main.item_of_company.view.*


class CustomInfoWindowAdapter( var context: Context) : GoogleMap.InfoWindowAdapter {

    private lateinit var inflater: LayoutInflater
    var model: Company? = null

    @SuppressLint("InflateParams")
    override fun getInfoContents(marker: Marker): View? {

        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.item_of_company, null)

        model = marker.tag as Company?

        if (model != null) {
            val pool : BitmapPool = Glide.get(context).bitmapPool
            if (model!!.name.equals("NambaTaxi")) {
                Glide.with(v.company_logo.getContext())
                        .load(model!!.icon)
                        .asBitmap()
                        .override(50, 50)
                        .animate(android.R.anim.fade_in)
                        .imageDecoder(SvgBitmapDecoder(pool))
                        .into(v.company_logo);
            } else {
                Glide.with(v.company_logo.getContext())
                        .load(model!!.icon)
                        .override(50, 50)
                        .into(v.company_logo)

            }


        }
        v.name.text = model!!.name
        v.phone.text = model!!.contacts!![0].contact
        v.address.text = model!!.contacts!![1].contact

        return v
    }

        override fun getInfoWindow(marker: Marker): View? {
            return null
        }
}
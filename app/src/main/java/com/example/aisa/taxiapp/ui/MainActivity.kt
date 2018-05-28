package com.example.aisa.taxiapp.ui


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.aisa.taxiapp.R
import com.example.aisa.taxiapp.ui.map.MapActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBtn()
    }

    private fun initBtn(){
        button_find.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }
}


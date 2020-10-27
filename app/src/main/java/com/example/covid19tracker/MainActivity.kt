package com.example.covid19tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, IndiaNumbersActivity::class.java)
        Log.i("Activity1", "Successful")
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
        }, 0)
    }
}
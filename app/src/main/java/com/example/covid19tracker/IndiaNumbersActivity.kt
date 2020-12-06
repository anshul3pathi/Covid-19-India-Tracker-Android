package com.example.covid19tracker

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import java.text.DecimalFormat

class IndiaNumbersActivity : AppCompatActivity() {

    lateinit var mIntent2: Intent

    fun startThirdActivity(view: View) {

        mIntent2 = Intent(this, StateWiseActivity::class.java)
        startActivity(mIntent2)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_india_numbers)

        val covid19ViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(CovidDataViewModel::class.java)

        covid19ViewModel.covid19LiveData!!.observe(this, { covid19Data->
            covid19Data!!.let {
                Log.d("IndiaNumberOne", "${covid19Data.size}")
                for(item in it) {
                    if(item.stateName == "India") {
                        Log.i("IndiaNumbersTest", item.stateName)
                        updateData(item)
                        break
                    }
                }
            }
        })

    }

     private fun updateData(data: StateData) {

        findViewById<TextView>(R.id.confirmedNumber).text = data.confirmed
        findViewById<TextView>(R.id.confirmedNumberIncrease).text = data.confirmedDelta

        findViewById<TextView>(R.id.activeNumber).text = data.active
        findViewById<TextView>(R.id.activeNumberIncrease).text = data.activeDelta

        findViewById<TextView>(R.id.recoveredNumber).text = data.recovered
        findViewById<TextView>(R.id.recoveredNumberIncrease).text = data.recoveredDelta

        findViewById<TextView>(R.id.deceasedNumber).text = data.deceased
        findViewById<TextView>(R.id.deceasedNumberIncrease).text = data.deceasedDelta

    }

}
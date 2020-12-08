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
import androidx.core.view.marginTop
import androidx.lifecycle.ViewModelProvider
import org.w3c.dom.Text
import java.text.DecimalFormat

class IndiaNumbersActivity : AppCompatActivity() {

    lateinit var mConfirmedDelta: TextView
    lateinit var mActiveDelta: TextView
    lateinit var mRecoveredDelta: TextView
    lateinit var mDeceasedDelta: TextView

    lateinit var mIntent2: Intent

    fun startThirdActivity(view: View) {

        mIntent2 = Intent(this, StateWiseActivity::class.java)
        startActivity(mIntent2)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_india_numbers)

        mConfirmedDelta = findViewById(R.id.confirmed_delta_text)
        mActiveDelta = findViewById(R.id.active_delta_text)
        mRecoveredDelta = findViewById(R.id.recovered_delta_text)
        mDeceasedDelta = findViewById(R.id.deceased_delta_text)

        val covid19ViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(CovidDataViewModel::class.java)

        covid19ViewModel.covid19LiveData!!.observe(this, { covid19Data ->
            covid19Data!!.let {
                Log.d("IndiaNumberOne", "${covid19Data.size}")
                for (item in it) {
                    if (item.stateName == "India") {
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
        if (data.confirmedDelta[0] == '-') {
            mConfirmedDelta.text = "-"
            findViewById<TextView>(R.id.confirmedNumberIncrease).text = data.confirmedDelta.substring(1)
        } else {
            mConfirmedDelta.text = "+"
            findViewById<TextView>(R.id.confirmedNumberIncrease).text = data.confirmedDelta
        }

        findViewById<TextView>(R.id.activeNumber).text = data.active
        if (data.activeDelta[0] == '-') {
            mActiveDelta.text = "-"
            findViewById<TextView>(R.id.activeNumberIncrease).text = data.activeDelta.substring(1)
        } else {
            mActiveDelta.text = "+"
            findViewById<TextView>(R.id.activeNumberIncrease).text = data.activeDelta
        }

        findViewById<TextView>(R.id.recoveredNumber).text = data.recovered
        if (data.recoveredDelta[0] == '-') {
            mRecoveredDelta.text = "-"
            findViewById<TextView>(R.id.recoveredNumberIncrease).text = data.recoveredDelta.substring(1)
        } else {
            mRecoveredDelta.text = "+"
            findViewById<TextView>(R.id.recoveredNumberIncrease).text = data.recoveredDelta
        }

        findViewById<TextView>(R.id.deceasedNumber).text = data.deceased
        if (data.deceasedDelta[0] == '-') {
            mDeceasedDelta.text = "-"
            findViewById<TextView>(R.id.deceasedNumberIncrease).text = data.deceasedDelta.substring(1)
        } else {
            mDeceasedDelta.text = "+"
            findViewById<TextView>(R.id.deceasedNumberIncrease).text = data.deceasedDelta
        }

        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        dateTextView.text = getString(R.string.as_on_date, splitDateAndState(data.id))
        dateTextView.visibility = View.VISIBLE

    }

}
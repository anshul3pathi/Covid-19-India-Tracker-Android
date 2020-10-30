package com.example.covid19tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_state_wise.*
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf

class IndiaNumbersActivity : AppCompatActivity() {

    lateinit var mIntent2: Intent

    fun startThirdActivity(view: View) {

        mIntent2 = Intent(this, StateWiseActivity::class.java)
        startActivity(mIntent2)

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_india_numbers)

        Log.i("Second Activity", "Successful")

        fetchIndiaData()

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

    private fun fetchIndiaData() {
        val url = "https://api.covid19india.org/v4/data-2020-10-28.json"
        val jsonRequestObject = JsonObjectRequest(Request.Method.GET, url, null,
            {
                val jsonObject = it.getJSONObject("TT")
                val total = jsonObject.getJSONObject("total")
                val delta = jsonObject.getJSONObject("delta")

                val confirmed = total.getString("confirmed")
                val recovered = total.getString("recovered")
                val deceased = total.getString("deceased")
                val active = (confirmed.toInt() - recovered.toInt() - deceased.toInt()).toString()

                val confirmedDelta = delta.getString("confirmed")
                val recoveredDelta = delta.getString("recovered")
                val deceasedDelta = delta.getString("deceased")
                val activeDelta = (confirmedDelta.toInt() - recoveredDelta.toInt() - deceasedDelta.toInt()).toString()

                val totalData: StateData = StateData(
                    "India",
                    confirmed,
                    active,
                    recovered,
                    deceased,
                    confirmedDelta,
                    activeDelta,
                    recoveredDelta,
                    deceasedDelta
                )
                updateData(totalData)
            },
            {
                Log.e("Api call", "API call in india number activity failed.")
            })
        MySingleton.getInstance(this).addToRequestQueue(jsonRequestObject)
    }

}
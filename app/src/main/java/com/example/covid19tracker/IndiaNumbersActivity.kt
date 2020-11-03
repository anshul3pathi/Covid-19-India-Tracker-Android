package com.example.covid19tracker

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_india_numbers.*
import java.text.DecimalFormat
import java.time.LocalDate
import kotlin.properties.Delegates

class IndiaNumbersActivity : AppCompatActivity() {

    lateinit var mIntent2: Intent

    var mSuccess: Boolean = false

    fun startThirdActivity(view: View) {

        mIntent2 = Intent(this, StateWiseActivity::class.java)
        startActivity(mIntent2)
    }

    fun numberFormatter(data: StateData): StateData {
        val myFormatter = DecimalFormat("##,###")
        return StateData(
            data.stateName,
            myFormatter.format(data.confirmed.toFloat()).toString(),
            myFormatter.format(data.active.toFloat()).toString(),
            myFormatter.format(data.recovered.toFloat()).toString(),
            myFormatter.format(data.deceased.toFloat()).toString(),
            myFormatter.format(data.confirmedDelta.toFloat()).toString(),
            myFormatter.format(data.activeDelta.toFloat()).toString(),
            myFormatter.format(data.recoveredDelta.toFloat()).toString(),
            myFormatter.format(data.deceasedDelta.toFloat()).toString(),
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_india_numbers)

        fetchIndiaData(0)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate(x: Long): String {
        return LocalDate.now().minusDays(x).toString()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchIndiaData(dayMinus: Long) {
        val date = getDate(dayMinus)
        val url = "https://api.covid19india.org/v4/data-$date.json"
        Log.i("URL API", url)
        val jsonRequestObject = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val jsonObject = response.getJSONObject("TT")
                val total = jsonObject.getJSONObject("total")
                val delta = jsonObject.getJSONObject("delta")

                val confirmed = total.getString("confirmed")
                val recovered = total.getString("recovered")
                val deceased = total.getString("deceased")
                val active = (
                        confirmed.toInt() - recovered.toInt() - deceased.toInt()
                 ).toString()

                val confirmedDelta = delta.getString("confirmed")
                val recoveredDelta = delta.getString("recovered")
                val deceasedDelta = delta.getString("deceased")
                val activeDelta = (
                        confirmedDelta.toInt() - recoveredDelta.toInt() - deceasedDelta.toInt()
                ).toString()

                var totalData: StateData = StateData(
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
                totalData = numberFormatter(totalData)

                val dateTextView = findViewById<TextView>(R.id.dateTextView)
                dateTextView.text = "as on date - $date"
                dateTextView.visibility = View.VISIBLE

                updateData(totalData)
            },
            Response.ErrorListener{ error->
                Log.e("Api call", "API call in india number activity failed.")
                Log.e("Api call Error", error.toString())
                fetchIndiaData(dayMinus + 1)
            })
        MySingleton.getInstance(this).addToRequestQueue(jsonRequestObject)
    }
}
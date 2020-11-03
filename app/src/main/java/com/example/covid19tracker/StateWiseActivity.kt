package com.example.covid19tracker

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_state_wise.*
import java.lang.Exception
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StateWiseActivity : AppCompatActivity() {

    private val stateNamesToCode: Map<String, String> = mapOf(
        "Andaman and Nicobar Islands" to "AN", "Andhra Pradsh" to "AP",
        "Arunachal Pradesh" to "AR", "Assam" to "AS", "Bihar" to "BR", "Chandigarh" to "CH",
        "Chattisgarh" to "CT", "Delhi" to "DL", "Dadra and Nagar Haveli" to "DN", "Goa" to "GA",
        "Gujrat" to "GJ", "Himachal Pradesh" to "HP", "Haryana" to "HR", "Jharkhand" to "JH",
        "Jammu and Kashmir" to "JK", "Karnataka" to "KA", "Kerala" to "KL", "Ladakh" to "LA",
        "Maharashtra" to "MH", "Meghalaya" to "ML", "Manipur" to "MN", "Madhya Pradesh" to "MP",
        "Mizoram" to "MZ", "Nagaland" to "NL", "Orissa" to "OR", "Punjab" to "PB",
        "Pudducherry" to "PY", "Rajasthan" to "RJ", "Sikkim" to "SK", "Telangana" to "TG",
        "Tamil Nadu" to "TN", "Tripura" to "TR", "India" to "TT", "Uttar Pradesh" to "UP",
        "Uttarakhand" to "UT", "West Bengal" to "WB"
    )

    lateinit var mAdapter: StateWiseListAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_wise)
        Log.i("Third Activity", "Loaded Successfully")
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = StateWiseListAdapter()

        recyclerView.adapter = mAdapter

        fetchData(0)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchData(dayMinus: Long) {
        val date = IndiaNumbersActivity().getDate(dayMinus)
        val url = "https://api.covid19india.org/v4/data-$date.json"
        val jSONObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener{ response ->
                val stateDataList = ArrayList<StateData>()
                Log.i("API CAll", "Successful")
                for(item in stateNamesToCode) {
                    val stateDataJsonObject = response.getJSONObject(item.value)
                    val total = stateDataJsonObject.getJSONObject("total")
                    val delta = stateDataJsonObject.getJSONObject("delta")

                    val confirmed = total.getString("confirmed")
                    val recovered = total.getString("recovered")
                    val deceased = total.getString("deceased")
                    val active = (confirmed.toInt() - recovered.toInt() - deceased.toInt()).toString()

                    val deltaConfirmed = delta.getString("confirmed")
                    val deltaRecovered = delta.getString("recovered")
                    lateinit var deltaDeceased: String
                    deltaDeceased = try{
                        delta.getString("deceased")
                    } catch(e: Exception) {
                        Log.e("Exception in api", e.toString())
                        "0"
                    }
                    val deltaActive = (deltaConfirmed.toInt() - deltaRecovered.toInt() - deltaDeceased.toInt()).toString()

                    Log.i("test", confirmed)

                    var state = StateData(
                        item.key,
                        confirmed,
                        active,
                        recovered,
                        deceased,
                        deltaConfirmed,
                        deltaActive,
                        deltaRecovered,
                        deltaDeceased
                    )
                    state = IndiaNumbersActivity().numberFormatter(state)

                    if(item.key == "India") {
                        continue
                    } else {
                        stateDataList.add(state)
                    }
                }
                mAdapter.updateCovidData(stateDataList)
                Toast.makeText(this, "$date", Toast.LENGTH_LONG).show()

            },
            Response.ErrorListener{ error->
                Log.e("Volley Failed", error.toString())
                fetchData(dayMinus + 1)
            })
        MySingleton.getInstance(this).addToRequestQueue(jSONObjectRequest)
    }
}
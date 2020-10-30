package com.example.covid19tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_state_wise.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StateWiseActivity : AppCompatActivity() {

    private val stateNamesToCode: Map<String, String> = mapOf(
        "Andaman and Nicobar Islands" to "AN", "Andhra Pradsh" to "AP", "Arunachal Pradesh" to "AR",
        "Assam" to "AS", "Bihar" to "BR", "Chandigarh" to "CH", "Chattisgarh" to "CT", "Delhi" to "DL",
        "Dadra and Nagar Haveli" to "DN", "Goa" to "GA", "Gujrat" to "GJ", "Himachal Pradesh" to "HP",
        "Haryana" to "HR", "Jharkhand" to "JH", "Jammu and Kashmir" to "JK", "Karnataka" to "KA",
        "Kerala" to "KL", "Ladakh" to "LA", "Maharashtra" to "MH", "Meghalaya" to "ML", "Manipur"
                to "MN", "Madhya Pradesh" to "MP", "Mizoram" to "MZ", "Nagaland" to "NL", "Orissa" to "OR",
        "Punjab" to "PB", "Pudducherry" to "PY", "Rajasthan" to "RJ", "Sikkim" to "SK", "Telangana"
                to "TG", "Tamil Nadu" to "TN", "Tripura" to "TR", "India" to "TT", "Uttar Pradesh" to "UP",
        "Uttarakhand" to "UT", "West Bengal" to "WB"
    )

    lateinit var mAdapter: StateWiseListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_wise)
        Log.i("Third Activity", "Loaded Successfully")
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = StateWiseListAdapter()
        fetchData()
        recyclerView.adapter = mAdapter

    }

    private fun fetchData() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date: String = sdf.format(Date())
        Log.i("Date", date)
//        val url = "https://api.covid19india.org/v4/data-$date.json"
        val url = "https://api.covid19india.org/v4/data-2020-10-28.json"
//        Log.i("URL", url)
        val jSONObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                val stateDataList = ArrayList<StateData>()
                Log.i("API CAll", "Successful")
                for(item in stateNamesToCode) {
                    val stateDataJsonObject = it.getJSONObject(item.value)
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

                    val state = StateData(
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
                    Log.i("Test4", "${state.confirmed} ${state.recovered} ${state.active} ${state.deceased}")

                    Log.i("Test5", "${state.confirmedDelta} ${state.recoveredDelta} ${state.deceasedDelta} ${state.activeDelta}")

                    if(item.key == "India") {
                        continue
                    } else {
                        stateDataList.add(state)
                    }
                }
                mAdapter.updateCovidData(stateDataList)
            },
            {
                Log.e("Volley Failed", "API Request Failed")
            })
        MySingleton.getInstance(this).addToRequestQueue(jSONObjectRequest)
    }

}
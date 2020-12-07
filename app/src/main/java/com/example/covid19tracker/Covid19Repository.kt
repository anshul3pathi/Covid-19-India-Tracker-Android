package com.example.covid19tracker

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

@RequiresApi(Build.VERSION_CODES.O)
class Covid19Repository(private val covid19dao: Covid19DataBaseDao,
                        private val application: Application) {

    private val repoJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + repoJob)
    private val _covid19LiveData = covid19dao.getAllStates()
    val covid19LiveData: LiveData<List<StateData>>
        get() = _covid19LiveData

    init {
        Log.i("Repository", "Object Created!")
        fetchIndiaData(0)
    }

    companion object {
        // Repository follows singleton pattern
        @Volatile
        private var INSTANCE: Covid19Repository? = null
        fun getCovid19Repository(covid19dao: Covid19DataBaseDao,
                                    application: Application): Covid19Repository {
            return INSTANCE?: synchronized(this) {
                val instance = Covid19Repository(covid19dao, application)
                INSTANCE = instance
                instance
            }
        }

        private var SUCCESS_DATE: String? = null
        private val STATE_NAMES_TO_CODES: Map<String, String> = mapOf(
            "Andaman and Nicobar Islands" to "AN", "Andhra Pradesh" to "AP",
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
    }

    private fun checkForUpdatingTable() {
        if(SUCCESS_DATE != null) {
            uiScope.launch {
                val check = covid19dao.checkForUpdatingTable("$SUCCESS_DATE ${"Bihar"}")
                if(check != null) {
//                    deleteOldData()

                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchIndiaData(dayMinus: Long) {
        val date = getDate(dayMinus)
        val url = "https://api.covid19india.org/v4/data-$date.json"
        Log.i("URL API", url)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response->
            Log.d("Volley Call", "Successful")
            SUCCESS_DATE = date
            parseData(response)

        }, { error->
            Log.e("Volley Call", error.message.toString())
            fetchIndiaData(dayMinus + 1)
        })
        MySingleton.getInstance(application.applicationContext).addToRequestQueue(jsonObjectRequest)
    }

    private fun parseData(response: JSONObject) {
        for(item in STATE_NAMES_TO_CODES) {
            val stateDataJsonObject = response.getJSONObject(item.value)
            val total = stateDataJsonObject.getJSONObject("total")
            val delta = stateDataJsonObject.getJSONObject("delta")

            val confirmed = parseDataUtil(total, "confirmed")
            val recovered = parseDataUtil(total, "recovered")
            val deceased = parseDataUtil(total, "deceased")
            val active = (confirmed.toInt() - recovered.toInt() - deceased.toInt()).toString()

            val deltaConfirmed = parseDataUtil(delta, "confirmed")
            val deltaRecovered = parseDataUtil(delta, "recovered")
            val deltaDeceased = parseDataUtil(delta, "deceased")
            val deltaActive = (deltaConfirmed.toInt() - deltaRecovered.toInt() - deltaDeceased.toInt()).toString()

            var stateData = StateData(
                "$SUCCESS_DATE ${item.key}",
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
            stateData = numberFormatter(stateData)
            uiScope.launch {
                covid19dao.insert(stateData)
            }
        }
        MySingleton.getInstance(application.applicationContext).cancelRequests(application)
    }

}
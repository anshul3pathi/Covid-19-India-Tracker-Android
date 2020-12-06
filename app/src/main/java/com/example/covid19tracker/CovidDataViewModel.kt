package com.example.covid19tracker

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import java.text.DecimalFormat
import java.time.LocalDate


//class MyViewModel : ViewModel() {
//    val covid19LiveData: LiveData<ArrayList<StateData>>
//    init {
//
//    }
//
//}

class VolleyCall constructor(context: Context) {

    private val mContext = context

    companion object {
        val COVID19DATA = ArrayList<StateData>()
    }
//    private val mViewModel = MyViewModel()

    private val mStateNamesToCode: Map<String, String> = mapOf(
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

    private fun numberFormatter(data: StateData): StateData {
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
    private fun getDate(x: Long): String {
        return LocalDate.now().minusDays(x).toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
     fun fetchIndiaData(dayMinus: Long) {
        val date = getDate(dayMinus)
        val url = "https://api.covid19india.org/v4/data-$date.json"
        Log.i("URL API", url)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response->
            Log.d("Volley Call", "Successful")
            parseData(response)
            Log.d("Covid19arraySize", COVID19DATA.size.toString())
//            callback.onSuccess()
        }, { error->
//            when(error.message.toString()) {
//                 "404 not Found"-> fetchIndiaData(dayMinus + 1)
//            }
            Log.e("Volley Call", error.message.toString())
            fetchIndiaData(dayMinus + 1)
        })
        MySingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest)
    }

    private fun parseData(response: JSONObject) {
        for(item in mStateNamesToCode) {
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

            val stateData = StateData(
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
            COVID19DATA.add(stateData)
        }
        Log.d("FetchData", "${COVID19DATA.size}")
    }

    private fun parseDataUtil(variable: JSONObject, key: String): String {
        return try {
            variable.getString(key)
        } catch(e: Exception) {
            "0"
        }
    }

}
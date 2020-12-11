package com.example.covid19tracker

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.covid19tracker.database.StateData
import com.example.covid19tracker.repository.Covid19Repository
import org.json.JSONObject
import java.text.DecimalFormat
import java.time.LocalDate



val STATE_NAMES_TO_CODES: Map<String, String> = mapOf(
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

fun numberFormatter(data: StateData): StateData {
    val myFormatter = DecimalFormat("##,###")
    return StateData(
        data.id,
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
fun getDate(x: Long): String {
    return LocalDate.now().minusDays(x).toString()
}

fun parseJsonDataUtil(json: JSONObject, key: String): JSONObject? {
    return try {
        json.getJSONObject(key)
    } catch(e: Exception) {
        null
    }
}

fun parseStringDataUtil(variable: JSONObject?, key: String): String {
    return if (variable != null) {
        try {
            variable.getString(key)
        } catch(e: Exception) {
            "0"
        }
    }
    else "0"
}

fun splitDateAndState(date: String): String {
    return date.split(" ")[0]
}

fun checkLatestDate(date1: String, date2: String): Boolean {
    return (date1 < date2)
}

fun parseData(response: JSONObject, date: String): ArrayList<StateData> {
    val covidData = ArrayList<StateData>()
    for(item in STATE_NAMES_TO_CODES) {
        val stateDataJsonObject = response.getJSONObject(item.value)
//            val total = stateDataJsonObject.getJSONObject("total")
//            val delta = stateDataJsonObject.getJSONObject("delta")
        val total = parseJsonDataUtil(stateDataJsonObject, "total")
        val delta = parseJsonDataUtil(stateDataJsonObject, "delta")

        val confirmed = parseStringDataUtil(total, "confirmed")
        val recovered = parseStringDataUtil(total, "recovered")
        val deceased = parseStringDataUtil(total, "deceased")
        val active = (confirmed.toInt() - recovered.toInt() - deceased.toInt()).toString()

        val deltaConfirmed = parseStringDataUtil(delta, "confirmed")
        val deltaRecovered = parseStringDataUtil(delta, "recovered")
        val deltaDeceased = parseStringDataUtil(delta, "deceased")
        val deltaActive = (deltaConfirmed.toInt() - deltaRecovered.toInt()
                - deltaDeceased.toInt()).toString()

        var stateData = StateData(
            "${date} ${item.key}",
            item.key,
            confirmed,
            active,
            recovered,
            deceased,
            deltaConfirmed,
            deltaActive,
            deltaRecovered,
            deltaDeceased,
        )
        stateData = numberFormatter(stateData)
        covidData.add(stateData)
    }
    return covidData
}

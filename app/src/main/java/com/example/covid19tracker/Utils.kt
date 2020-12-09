package com.example.covid19tracker

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.covid19tracker.database.StateData
import org.json.JSONObject
import java.text.DecimalFormat
import java.time.LocalDate

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
    Log.d("Utils", "$date1 and $date2")
    return (date1 < date2)
}

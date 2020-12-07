package com.example.covid19tracker

import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.text.DecimalFormat
import java.time.LocalDate

fun numberFormatter(data: StateData): StateData {
    val myFormatter = DecimalFormat("##,###")
    return StateData(
        data.date,
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

fun parseDataUtil(variable: JSONObject, key: String): String {
    return try {
        variable.getString(key)
    } catch(e: Exception) {
        "0"
    }
}
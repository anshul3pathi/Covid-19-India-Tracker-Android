package com.example.covid19tracker.repository

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.android.volley.ClientError
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.covid19tracker.*
import com.example.covid19tracker.database.Covid19DataBaseDao
import com.example.covid19tracker.database.StateData
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
        fetchIndiaData(0)
    }

    companion object {
        // Repository follows singleton pattern
        @Volatile
        private var INSTANCE: Covid19Repository? = null
        fun getCovid19Repository(
            covid19dao: Covid19DataBaseDao,
            application: Application
        ): Covid19Repository {
            return INSTANCE ?: synchronized(this) {
                val instance = Covid19Repository(covid19dao, application)
                INSTANCE = instance
                instance
            }
        }

        private var SUCCESS_DATE: String? = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchIndiaData(dayMinus: Long) {
        val date = getDate(dayMinus)
        val url = "https://api.covid19india.org/v4/data-$date.json"
        Log.i("URL API", url)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response->
            Log.d("Repository Volley Call", "Successful")
            SUCCESS_DATE = date
            checkForUpdatingDatabase(response)

        }, { error->
            Log.e("Repository Volley Call", error.toString())
            if(error is ClientError) {
                fetchIndiaData(dayMinus + 1)
            }
        })
        MySingleton.getInstance(application.applicationContext).addToRequestQueue(jsonObjectRequest)
    }

    private fun checkForUpdatingDatabase(response: JSONObject) {
        var covidData: ArrayList<StateData>? = null
        if(_covid19LiveData.value!!.isNotEmpty()) {
            val currentIdInDatabase = _covid19LiveData.value!![0].id
            val currentDateInDatabase = splitDateAndState(currentIdInDatabase)
            when (checkLatestDate(currentDateInDatabase, SUCCESS_DATE!!)) {
                true -> {
                    Log.i("RepositoryCheck", "Updating data in the database!")
                    deleteExistingDataInDatabase()
                    covidData = parseData(response, SUCCESS_DATE!!)
                }
                else -> Log.i("RepositoryCheck", "Data in database up to date!")
            }
        }
        else {
            Log.i("RepositoryCheck", "Live data was null so new data is inserted.")
            covidData = parseData(response, SUCCESS_DATE!!)
        }

        if(covidData != null) {
            uiScope.launch {
                covid19dao.insertAll(covidData)
                Log.d("repository", "data insertion successful!")
            }
        }

    }

    private fun deleteExistingDataInDatabase() {
        uiScope.launch {
            covid19dao.deleteOldTable()
        }
        Log.i("RepositoryDelete", "Old data has been deleted!")
    }

}
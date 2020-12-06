package com.example.covid19tracker

import android.app.Application
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


@RequiresApi(Build.VERSION_CODES.O)
class CovidDataViewModel(application: Application) : AndroidViewModel(application) {

    val covid19LiveData: LiveData<List<StateData>>
    private val covid19dao = Covid19DataBase.getDatabase(application).covidDataBaseDao
    @RequiresApi(Build.VERSION_CODES.O)
    private val repo = Covid19Repository(covid19dao, application)

    init {
        covid19LiveData = repo.covid19LiveData
    }

}
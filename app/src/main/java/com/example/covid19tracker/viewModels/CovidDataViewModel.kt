package com.example.covid19tracker.viewModels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.covid19tracker.database.Covid19DataBase
import com.example.covid19tracker.database.StateData
import com.example.covid19tracker.repository.Covid19Repository


@RequiresApi(Build.VERSION_CODES.O)
class CovidDataViewModel(application: Application) : AndroidViewModel(application) {

    val covid19LiveData: LiveData<List<StateData>>
    private val covid19dao = Covid19DataBase.getDatabase(application).covidDataBaseDao
    @RequiresApi(Build.VERSION_CODES.O)
    private val repo = Covid19Repository.getCovid19Repository(covid19dao, application)

    init {
        covid19LiveData = repo.covid19LiveData
        Log.i("ViewModel", "Object Created!")
    }

}
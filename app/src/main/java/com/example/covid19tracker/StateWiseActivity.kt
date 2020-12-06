package com.example.covid19tracker

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_state_wise.*
import java.lang.Exception
import kotlin.collections.ArrayList

class StateWiseActivity : AppCompatActivity() {

    lateinit var mAdapter: StateWiseListAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_wise)
        Log.i("Third Activity", "Loaded Successfully")
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = StateWiseListAdapter()
        recyclerView.adapter = mAdapter

        val covid19ViewModel = ViewModelProvider(this).get(CovidDataViewModel::class.java)
        covid19ViewModel.covid19LiveData.observe(this, { list->
            Log.d("StateWiseActivityOne", list.size.toString())
            list?.let {
                Log.d("StateWiseActivity", it.size.toString())
                mAdapter.updateCovidData(it)
            }
        })

    }

//
}
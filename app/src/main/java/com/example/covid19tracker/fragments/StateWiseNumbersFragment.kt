package com.example.covid19tracker.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covid19tracker.R
import com.example.covid19tracker.adapters.StateWiseListAdapter
import com.example.covid19tracker.viewModels.CovidDataViewModel
import kotlinx.android.synthetic.main.activity_state_wise.view.*
class StateWiseNumbersFragment : Fragment() {

    lateinit var mAdapter: StateWiseListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.activity_state_wise, container, false)
        rootView.recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        mAdapter = StateWiseListAdapter()
        rootView.recyclerView.adapter = mAdapter

        val covid19ViewModel = ViewModelProvider(this).get(CovidDataViewModel::class.java)
        covid19ViewModel.covid19LiveData.observe(viewLifecycleOwner, { list->
            list?.let {
                mAdapter.updateCovidData(it)
            }
        })

        return rootView
    }
}
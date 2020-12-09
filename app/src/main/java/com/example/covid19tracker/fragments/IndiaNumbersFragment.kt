package com.example.covid19tracker.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.covid19tracker.R
import com.example.covid19tracker.database.StateData
import com.example.covid19tracker.splitDateAndState
import com.example.covid19tracker.viewModels.CovidDataViewModel

private val TAB_NAMES = listOf("India", "States")

class IndiaNumbersFragment : Fragment() {

    lateinit var mConfirmedDelta: TextView
    lateinit var mActiveDelta: TextView
    lateinit var mRecoveredDelta: TextView
    lateinit var mDeceasedDelta: TextView

    lateinit var mRootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d("IndiaNumbersFragment", "Fragment Created!")
        // Inflate the layout for this fragment
        mRootView =  inflater.inflate(R.layout.activity_india_numbers, container, false)

        mConfirmedDelta = mRootView.findViewById(R.id.confirmed_delta_text)
        mActiveDelta = mRootView.findViewById(R.id.active_delta_text)
        mRecoveredDelta = mRootView.findViewById(R.id.recovered_delta_text)
        mDeceasedDelta = mRootView.findViewById(R.id.deceased_delta_text)

        val covid19ViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(CovidDataViewModel::class.java)

        covid19ViewModel.covid19LiveData!!.observe(viewLifecycleOwner, { covid19Data ->
            covid19Data!!.let {
                Log.d("IndiaNumberOne", "${covid19Data.size}")
                for (item in it) {
                    if (item.stateName == "India") {
                        Log.i("IndiaNumbersTest", item.stateName)
                        updateData(item)
                        break
                    }
                }
            }
        })
        return mRootView
    }

    private fun updateData(data: StateData) {

        mRootView.findViewById<TextView>(R.id.confirmedNumber).text = data.confirmed
        if (data.confirmedDelta[0] == '-') {
            mConfirmedDelta.text = "-"
            mRootView.findViewById<TextView>(R.id.confirmedNumberIncrease).text = data.confirmedDelta.substring(1)
        } else {
            mConfirmedDelta.text = "+"
            mRootView.findViewById<TextView>(R.id.confirmedNumberIncrease).text = data.confirmedDelta
        }

        mRootView.findViewById<TextView>(R.id.activeNumber).text = data.active
        if (data.activeDelta[0] == '-') {
            mActiveDelta.text = "-"
            mRootView.findViewById<TextView>(R.id.activeNumberIncrease).text = data.activeDelta.substring(1)
        } else {
            mActiveDelta.text = "+"
            mRootView.findViewById<TextView>(R.id.activeNumberIncrease).text = data.activeDelta
        }

        mRootView.findViewById<TextView>(R.id.recoveredNumber).text = data.recovered
        if (data.recoveredDelta[0] == '-') {
            mRecoveredDelta.text = "-"
            mRootView.findViewById<TextView>(R.id.recoveredNumberIncrease).text = data.recoveredDelta.substring(1)
        } else {
            mRecoveredDelta.text = "+"
            mRootView.findViewById<TextView>(R.id.recoveredNumberIncrease).text = data.recoveredDelta
        }

        mRootView.findViewById<TextView>(R.id.deceasedNumber).text = data.deceased
        if (data.deceasedDelta[0] == '-') {
            mDeceasedDelta.text = "-"
            mRootView.findViewById<TextView>(R.id.deceasedNumberIncrease).text = data.deceasedDelta.substring(1)
        } else {
            mDeceasedDelta.text = "+"
            mRootView.findViewById<TextView>(R.id.deceasedNumberIncrease).text = data.deceasedDelta
        }

        val dateTextView = mRootView.findViewById<TextView>(R.id.dateTextView)
        dateTextView.text = getString(R.string.as_on_date, splitDateAndState(data.id))
        dateTextView.visibility = View.VISIBLE

    }
}

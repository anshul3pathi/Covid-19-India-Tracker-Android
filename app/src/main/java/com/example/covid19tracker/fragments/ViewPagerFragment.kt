package com.example.covid19tracker.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covid19tracker.R
import com.example.covid19tracker.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_view_pager.view.*

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentsList = arrayListOf(IndiaNumbersFragment(), StateWiseNumbersFragment())

        val viewPagerAdapter = ViewPagerAdapter(
            fragmentsList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        val tabTitles = listOf("India", "State")

        rootView.view_pager.adapter = viewPagerAdapter

        TabLayoutMediator(rootView.tab_layout, rootView.view_pager) { tab, position ->
            Log.d("ViewPager", "$position")
            tab.text = tabTitles[position]
            rootView.view_pager.setCurrentItem(tab.position, true)
        }.attach()

        return rootView
    }
}
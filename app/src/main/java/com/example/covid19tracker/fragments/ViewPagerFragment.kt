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

    private val mFragmentsList = arrayListOf(IndiaNumbersFragment(), StateWiseNumbersFragment())

    var mViewPagerAdapter: ViewPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_view_pager, container, false)


        mViewPagerAdapter = ViewPagerAdapter(
            mFragmentsList,
            this.childFragmentManager,
            lifecycle
        )

        val tabTitles = listOf("India", "State")

        rootView.view_pager.adapter = mViewPagerAdapter

        TabLayoutMediator(rootView.tab_layout, rootView.view_pager) { tab, position ->
            tab.text = tabTitles[position]
            rootView.view_pager.setCurrentItem(tab.position, true)
        }.attach()

        return rootView
    }
}
package com.example.covid19tracker.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.covid19tracker.R
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(savedInstanceState == null) {
            Timer().schedule(3000){
                findNavController().navigate(R.id.action_splashScreenFragment2_to_viewPagerFragment2)
            }
        }
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }
}

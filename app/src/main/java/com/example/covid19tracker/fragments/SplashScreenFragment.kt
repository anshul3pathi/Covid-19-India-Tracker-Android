package com.example.covid19tracker.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.covid19tracker.R
class SplashScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Handler().postDelayed ({
            findNavController().navigate(R.id.action_splashScreenFragment2_to_viewPagerFragment2)
        }, 3000)
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }
}
package com.example.moonraker_android.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.moonraker_android.R
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result;

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)

        textView.text = getString(R.string.prefs_test_string, prefs.getString("moonraker_ip", "undefined"), prefs.getString("moonraker_port", "undefined"))

        return root

    }
}
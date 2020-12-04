package com.example.moonraker_android.ui.temperatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.moonraker_android.R

class TemperaturesFragment : Fragment() {

    private lateinit var temperaturesViewModel: TemperaturesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        temperaturesViewModel =
            ViewModelProviders.of(this).get(TemperaturesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_temperatures, container, false)
        val textView: TextView = root.findViewById(R.id.text_temperatures)
        temperaturesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
package com.example.moonraker_android.ui.temperatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.moonraker_android.MainActivity
import com.example.moonraker_android.R

class TemperaturesFragment : Fragment() {


    lateinit var mainActivity: MainActivity

    private val viewModel: TemperaturesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity

        return inflater.inflate(R.layout.fragment_temperatures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateState()
    }

    private fun updateState() {
        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        sleep(1000)
                        viewModel.loadStatus(mainActivity.heaterBeds, mainActivity.extruders)
                    }
                } catch (e: InterruptedException) {
                }
            }
        }

        thread.start()
    }


}
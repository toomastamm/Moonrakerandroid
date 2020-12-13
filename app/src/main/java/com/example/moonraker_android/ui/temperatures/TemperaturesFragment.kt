package com.example.moonraker_android.ui.temperatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.moonraker_android.MainActivity
import com.example.moonraker_android.R
import com.example.moonraker_android.api.MoonrakerService
import kotlinx.android.synthetic.main.fragment_print_status.*
import kotlinx.android.synthetic.main.fragment_temperatures.*

class TemperaturesFragment : Fragment() {


    lateinit var mainActivity: MainActivity

    private val viewModel: TemperaturesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity

        viewModel.state.observe(viewLifecycleOwner, { item ->
            item.heater_beds["heater_bed"]?.current.let {
                plate_temp.text = String.format("%.2f C", it)
            }
            item.heater_beds["heater_bed"]?.target.let {
                plate_target.text = String.format("%.2f C", it)
            }
            item.extruders["extruder"]?.current.let {
                hotend_temp.text = String.format("%.2f C", it)
            }
            item.extruders["extruder"]?.target.let {
                hotend_target.text = String.format("%.2f C", it)
            }
        })



        return inflater.inflate(R.layout.fragment_temperatures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_set_hotend.setOnClickListener {
            TemperaturesAPI.setTemperatureTarget("extruder", editText_hotend.text.toString())
        }

        button_set_plate.setOnClickListener {
            TemperaturesAPI.setTemperatureTarget("heater_bed", editText_plate.text.toString())
        }


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
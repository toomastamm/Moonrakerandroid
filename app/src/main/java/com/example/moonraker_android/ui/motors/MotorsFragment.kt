package com.example.moonraker_android.ui.motors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.moonraker_android.MainActivity
import com.example.moonraker_android.R
import com.example.moonraker_android.api.MoonrakerService
import com.example.moonraker_android.ui.print_status.PrintStatusViewModel
import kotlinx.android.synthetic.main.fragment_print_status.*


class MotorsFragment : Fragment() {

    private val viewModel: MotorsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_motors, container, false)
        val textView: TextView = root.findViewById(R.id.text_motors)

        viewModel.state.observe(viewLifecycleOwner, { item ->
            textView.text = "X: ${item.X} \nY: ${item.Y} \nZ: ${item.Z} \nE: ${item.E}"
        })

        return root

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
                        viewModel.loadStatus()
                    }
                } catch (e: InterruptedException) {
                }
            }
        }

        thread.start()
    }
}
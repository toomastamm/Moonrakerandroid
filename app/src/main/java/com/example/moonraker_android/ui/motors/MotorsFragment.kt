package com.example.moonraker_android.ui.motors

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.moonraker_android.MainActivity
import com.example.moonraker_android.R
import com.example.moonraker_android.api.MoonrakerService
import com.example.moonraker_android.ui.print_status.PrintStatusViewModel
import kotlinx.android.synthetic.main.fragment_print_status.*


class MotorsFragment : Fragment(), View.OnClickListener {

    private val viewModel: MotorsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_motors, container, false)
        val xLabel: TextView = root.findViewById(R.id.x_label)
        val yLabel: TextView = root.findViewById(R.id.y_label)
        val zLabel: TextView = root.findViewById(R.id.z_label)
        val eLabel: TextView = root.findViewById(R.id.e_label)

        viewModel.state.observe(viewLifecycleOwner, { item ->
            xLabel.text = String.format("%.2f", item.X)
            yLabel.text = String.format("%.2f", item.Y)
            zLabel.text = String.format("%.2f", item.Z)
            eLabel.text = String.format("%.2f", item.E)
        })

        var group: ConstraintLayout = root.findViewById(R.id.motors_root);
        for (i in 0..group.childCount) {
            var v = group.getChildAt(i);
            if (v is Button) v.setOnClickListener(this)
        }
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

    override fun onClick(v: View?) {
        if (v != null) {
            view?.resources?.let {
                val name = it.getResourceName(v.id).split("/")[1]
                val split = name.split("_")
                var axis = split[0]
                var direction = split[1]
                var amount = split[2]

                MotorsAPI.moveMotor(axis, direction, amount)
            }
        }
    }
}
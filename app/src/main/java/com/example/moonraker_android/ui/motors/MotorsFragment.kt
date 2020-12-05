package com.example.moonraker_android.ui.motors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.moonraker_android.MainActivity
import com.example.moonraker_android.R


class MotorsFragment : Fragment() {

    private lateinit var motorsViewModel: MotorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        motorsViewModel =
            ViewModelProviders.of(this).get(MotorsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_motors, container, false)
        val textView: TextView = root.findViewById(R.id.text_motors)

        val activity: MainActivity? = activity as MainActivity?


        motorsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        sleep(1000)

                        var position = activity?.latestUpdate?.getJSONObject("toolhead")
                            ?.getJSONArray("position")
                        if (position != null) {
                            activity?.runOnUiThread {
                                textView.text =
                                    "X:${position[0]} Y:${position[1]}, Z: ${position[2]} E:${position[3]}"
                            }
                        }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }

        thread.start()


        return root
    }
}
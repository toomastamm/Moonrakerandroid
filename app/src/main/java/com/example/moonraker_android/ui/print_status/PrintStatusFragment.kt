package com.example.moonraker_android.ui.print_status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.moonraker_android.R

class PrintStatusFragment : Fragment() {

    private lateinit var printStatusViewModel: PrintStatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        printStatusViewModel =
            ViewModelProviders.of(this).get(PrintStatusViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_print_status, container, false)
        val textView: TextView = root.findViewById(R.id.text_print_status)
        printStatusViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
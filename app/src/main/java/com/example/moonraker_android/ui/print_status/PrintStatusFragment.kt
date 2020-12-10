package com.example.moonraker_android.ui.print_status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.moonraker_android.R
import kotlinx.android.synthetic.main.fragment_print_status.*

class PrintStatusFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
    private val model: PrintStatusViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.getState().observe(viewLifecycleOwner, { item ->
            // Update the UI
            text_state.text = item
        })
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
//
//        printStatusViewModel =
//            ViewModelProviders.of(this).get(PrintStatusViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_print_status, container, false)
//        val textView: TextView = root.findViewById(R.id.text_state)
//        printStatusViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = getString(R.string.prefs_test_string, prefs.getString("moonraker_ip", "undefined"), prefs.getString("moonraker_port", "undefined"))
//        })
//        return root
//    }
}
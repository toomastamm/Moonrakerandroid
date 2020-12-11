package com.example.moonraker_android.ui.print_status

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.moonraker_android.R
import kotlinx.android.synthetic.main.fragment_print_status.*

class PrintStatusFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
    private val viewModel: PrintStatusViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, { item ->
            text_state.text = item.state
            text_file.text = item.filename
        })
        return inflater.inflate(R.layout.fragment_print_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateState()
    }

    private fun updateState() {
        val thread: Thread = object : Thread() {
            override fun run() {
                viewModel.loadStatus()
            }
        }

        thread.start()
    }
}
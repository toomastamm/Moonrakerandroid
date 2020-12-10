package com.example.moonraker_android.ui.print_status

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.moonraker_android.R
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
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
        val root = inflater.inflate(R.layout.fragment_print_status, container, false)

        Log.d("PrintStatusFragment", "View created")

        viewModel.state.observe(viewLifecycleOwner, { item ->
            text_state.text = item
        })
        viewModel.file.observe(viewLifecycleOwner, { newFile ->
            text_file.text = newFile.toString()
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
                viewModel.loadState()
                viewModel.loadFile()
            }
        }

        thread.start()
    }
}
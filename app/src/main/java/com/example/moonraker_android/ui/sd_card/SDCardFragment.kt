package com.example.moonraker_android.ui.sd_card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.moonraker_android.R

class SDCardFragment : Fragment() {

    private lateinit var sdCardViewModel: SDCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sdCardViewModel =
            ViewModelProviders.of(this).get(SDCardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sd_card, container, false)
        val textView: TextView = root.findViewById(R.id.text_sd_card)
        sdCardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
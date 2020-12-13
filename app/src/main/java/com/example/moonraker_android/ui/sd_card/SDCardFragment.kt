package com.example.moonraker_android.ui.sd_card

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.work.*
import com.example.moonraker_android.R
import com.example.moonraker_android.worker.PrintWorker
import kotlinx.android.synthetic.main.fragment_sd_card.*

class SDCardFragment : Fragment() {

    private val viewModel: SDCardViewModel by activityViewModels()
    private val fileMap = hashMapOf<String, SDCardFileResponse>()
    private var fileList = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, { item ->
            fileList = arrayListOf() // reset the list when new files are added to the SD card

            for (i in 0 until item.size) {
                val file = item[i]
                fileMap[file.filename] = file
                fileList.add(file.filename)
            }
            setFileInfo(fileList[0])

            val fileAdapter = ArrayAdapter(
                activity as Context,
                android.R.layout.simple_list_item_1,
                fileList
            )
            spinner_files.adapter = fileAdapter
            spinner_files.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    setFileInfo(fileList[spinner_files.selectedItemPosition])
                    val fileIndex = spinner_files.selectedItemPosition

                    setFileInfo(fileList[fileIndex])
                    updateMetaDataState(fileList[fileIndex])
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {

                }
            }
        })
        viewModel.metaData.observe(viewLifecycleOwner, { item ->
            text_slicer.text = item.slicerName
            text_slicer_version.text = item.slicerVersion
            text_estimated_time.text = item.estimatedTime
        })
        return inflater.inflate(R.layout.fragment_sd_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        printButton.setOnClickListener {
            startPrint(fileList[spinner_files.selectedItemPosition])
        }
        updateFilesState()
    }

    private fun startPrint(file: String) {
        SDCardAPI.printFile(file, activity as Context)
//        val workerData = workDataOf(
//            PrintWorker.ESTIMATED_TIME to )
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val worker = OneTimeWorkRequestBuilder<PrintWorker>()
            .setConstraints(constraints)
//            .setInputData(workerData)
            .build()
    }

    private fun setFileInfo(fileName: String) {
        text_modified.text = fileMap[fileName]?.modified.toString()
        text_size.text = fileMap[fileName]?.size.toString()
    }

    private fun updateFilesState() {
        val thread: Thread = object : Thread() {
            override fun run() {
                viewModel.loadFiles()
            }
        }
        thread.start()
    }

    private fun updateMetaDataState(fileName: String) {
        val thread: Thread = object : Thread() {
            override fun run() {
                viewModel.loadFileDetails(fileName)
            }
        }
        thread.start()
    }
}
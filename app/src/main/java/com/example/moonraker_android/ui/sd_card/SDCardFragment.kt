package com.example.moonraker_android.ui.sd_card

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.moonraker_android.R
import com.example.moonraker_android.worker.PrintWorker
import kotlinx.android.synthetic.main.fragment_sd_card.*

class SDCardFragment : Fragment() {
    private var fileList = arrayListOf<String>()
    private lateinit var viewModel: SDCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SDCardViewModel::class.java)
        return inflater.inflate(R.layout.fragment_sd_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // NOT OK
        super.onViewCreated(view, savedInstanceState)
        text_modified.text = " "
        text_size.text = " "
        text_slicer.text = " "
        text_slicer_version.text = " "
        text_estimated_time.text = " "
        viewModel.state.observe(viewLifecycleOwner, { item ->
            Log.d("SDCardAPI", "SDCardFragment, file: $item") // 2 TIMES, WORKS
            fileList = arrayListOf() // reset the list when new files are added to the SD card

            if (item.size != 0) {
                for (i in 0 until item.size) {
                    val file = item[i]
                    Log.d("SDCardAPI", "file: $file")
                    viewModel.addFile(file)
                    fileList.add(file.filename)
                    viewModel.loadFileDetails(file.filename)
                }
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
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    text_modified.text = " "
                    text_size.text = " "
                    text_slicer.text = " "
                    text_slicer_version.text = " "
                    text_estimated_time.text = " "
                    val fileIndex = spinner_files.selectedItemPosition
                    setFileInfo(fileList[fileIndex])
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {

                }
            }
        })
        viewModel.metaData.observe(viewLifecycleOwner, { item -> // DONT WORK
            Log.d("SDCardAPI", "SDCardFragment, metadata: $item")
            viewModel.addFileMetadata(item)
            val fileName = item.fileName
            val file = viewModel.getFileByName(fileName)

            val fileIndex = fileList.indexOf(fileName)
            spinner_files.setSelection(fileIndex)

            text_size.text = file.size
            text_modified.text = file.modified
            text_slicer.text = item.slicerName
            text_slicer_version.text = item.slicerVersion
            text_estimated_time.text = item.estimatedTime
        })
        printButton.setOnClickListener {
            startPrint(fileList[spinner_files.selectedItemPosition])
        }
        // For debugging. Button clears metadata.
//        deleteButton.setOnClickListener {
//            viewModel.clearFileMetadata()
//        }
        updateFilesState()
    }

    private fun startPrint(file: String) {
        SDCardAPI.printFile(file, activity as Context)
        val workerData = workDataOf(
            PrintWorker.INPUT_ESTIMATED_TIME to text_estimated_time.text)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = OneTimeWorkRequestBuilder<PrintWorker>()
            .setConstraints(constraints)
            .setInputData(workerData)
            .build()
        WorkManager.getInstance(activity as Context).enqueue(workRequest)
    }

    private fun setFileInfo(fileName: String) {
        Log.d("SDCardAPI", "SDCardFragment, setFileInfo start: $fileName")
        val file = viewModel.getFileByName(fileName)
        val fileMetaData = viewModel.getFileMetadataByFileName(fileName)

        if (fileMetaData != null) {
            text_modified.text = file.modified
            text_size.text = file.size
            Log.d("SDCardAPI", "SDCardFragment, setFileInfo: $fileMetaData")
            text_slicer.text = fileMetaData.slicerName
            text_slicer_version.text = fileMetaData.slicerVersion
            text_estimated_time.text = fileMetaData.estimatedTime
        } else {
            viewModel.loadFileDetails(fileName)
        }
    }

    private fun updateFilesState() {
        val thread: Thread = object : Thread() {
            override fun run() {
                viewModel.loadFiles()
                fileList = viewModel.getFileNames()
            }
        }
        thread.start()
    }
}
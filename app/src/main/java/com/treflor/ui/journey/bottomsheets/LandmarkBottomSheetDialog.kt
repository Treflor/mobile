package com.treflor.ui.journey.bottomsheets

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.treflor.R
import kotlinx.android.synthetic.main.bottomsheet_add_landmark.*

class LandmarkBottomSheetDialog : BottomSheetDialogFragment() {

    var listener: LandmarkBottomSheetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_add_landmark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner_landmark_type.adapter = object : ArrayAdapter<String>(
            this.context!!, android.R.layout.simple_spinner_dropdown_item,
            listOf(
                "Fountain",
                "Caves",
                "Waterfall",
                "Rare plant",
                "Rare Animal/s",
                "Ancient settlements"
            )
        ) {}
        btn_add_landmark.setOnClickListener {
            listener?.onSave(
                et_landmark_title.text.toString(),
                et_landmark_snippet.text.toString(),
                spinner_landmark_type.selectedItem.toString()
            )
        }

        btn_cancel.setOnClickListener { dismiss() }
    }

    fun setLandmarkBottomSheetListener(landmarkBottomSheetListener: LandmarkBottomSheetListener) {
        this.listener = landmarkBottomSheetListener
    }

    override fun onDismiss(dialog: DialogInterface) {
        listener?.onDismiss(dialog)
        super.onDismiss(dialog)
    }

    interface LandmarkBottomSheetListener {
        fun onDismiss(dialog: DialogInterface)
        fun onSave(title: String, snippet: String, type: String)
    }
}
package com.treflor.ui.journey.bottomsheets

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.treflor.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
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

    private var images: List<Image>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner_landmark_type.adapter = object : ArrayAdapter<String>(
            this.context!!, android.R.layout.simple_spinner_dropdown_item,
            listOf(
                "Sanitary",
                "Fountain",
                "Caves",
                "Waterfall",
                "Rare plant",
                "Rare Animal/s",
                "Ancient settlements"
            )
        ) {}
        btn_add_landmark.setOnClickListener {

            if (et_landmark_title.text.isNullOrEmpty()) {
                til_landmark_title.error = "Title can't be empty!"
                return@setOnClickListener
            } else {
                til_landmark_title.error = null
            }

            if (et_landmark_snippet.text.isNullOrEmpty()) {
                til_landmark_snippet.error = "Description can't be empty!"
                return@setOnClickListener
            } else {
                til_landmark_snippet.error = null
            }

            listener?.onSave(
                et_landmark_title.text.toString(),
                et_landmark_snippet.text.toString(),
                spinner_landmark_type.selectedItem.toString(),
                images?.getImagePaths(),
                this
            )
        }

        btn_cancel.setOnClickListener { dismiss() }

        btn_add_landmark_photos.setOnClickListener {
            ImagePicker.create(this@LandmarkBottomSheetDialog)
                .folderMode(true)
                .toolbarImageTitle("Select Images")
                .multi()
                .limit(10)
                .imageDirectory("treflor")
                .start()
        }
    }

    fun setLandmarkBottomSheetListener(landmarkBottomSheetListener: LandmarkBottomSheetListener) {
        this.listener = landmarkBottomSheetListener
    }

    override fun onDismiss(dialog: DialogInterface) {
        listener?.onDismiss(dialog)
        super.onDismiss(dialog)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            var images = ImagePicker.getImages(data)
            this.images = images
            initRecyclerView(images.toImageItems())
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    interface LandmarkBottomSheetListener {
        fun onDismiss(dialog: DialogInterface)
        fun onSave(
            title: String,
            snippet: String,
            type: String,
            imagesPaths: List<String>?,
            bottomSheetDialog: LandmarkBottomSheetDialog
        )
    }

    private fun initRecyclerView(items: List<ImageItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
//            setOnItemClickListener { item, _ ->
//                (item as? JourneyItem)?.let {
//                    val actionDetail =
//                        HomeFragmentDirections.actionHomeFragmentToJourneyDetailsFragment(it.journeyResponse.id)
//
//                    navController.navigate(actionDetail)
//                }
//            }
        }
        rv_images.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = groupAdapter
        }
    }

    private fun List<Image>.toImageItems(): List<ImageItem> {
        return this.map {
            ImageItem(it, context!!)
        }
    }

    private fun List<Image>.getImagePaths(): List<String> {
        return this.map { it.path }
    }
}
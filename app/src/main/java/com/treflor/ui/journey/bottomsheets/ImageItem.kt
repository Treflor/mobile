package com.treflor.ui.journey.bottomsheets

import android.content.Context
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.model.Image
import com.treflor.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.image_row.*

class ImageItem(private val image: Image, val context: Context) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            // load image in to imageview
            Glide.with(context)
                .load(image.path)
                .centerCrop()
                .into(image_view)
        }
    }

    override fun getLayout() = R.layout.image_row
}
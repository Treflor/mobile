package com.treflor.ui.home.detailed

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.treflor.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.image_row_journey_details.*


class ImageItem(private val path: String, val context: Context) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            // load image in to imageview
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.background)
                .error(R.drawable.background)
            Glide.with(context).load(path).apply(options)
                .into(image_view)

        }
    }

    override fun getLayout() = R.layout.image_row_journey_details
}
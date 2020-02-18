package com.treflor.ui.home.list

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.treflor.R
import com.treflor.data.remote.response.JourneyResponse
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.journey_row.*


class JourneyItem(
    val journeyResponse: JourneyResponse,
    private val userId: String?,
    private val context: Context
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.background)
                .error(R.drawable.background)
            Glide.with(context).load(journeyResponse.journey?.image).apply(options)
                .into(img_row_background)

            tv_title.text = journeyResponse.journey?.title
            tv_difficulty.text = journeyResponse.journey?.level
            userId?.let {
                journeyResponse.favorites?.let {
                    if (it.contains(userId)) {
                        img_btn_favorite.setImageResource(R.drawable.ic_favorite)
                    } else {
                        img_btn_favorite.setImageResource(R.drawable.ic_favorite_border)
                    }
                }
            }
        }
    }

    override fun getLayout() = R.layout.journey_row
}
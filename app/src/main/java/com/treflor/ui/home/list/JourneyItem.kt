package com.treflor.ui.home.list

import com.treflor.R
import com.treflor.data.remote.response.JourneyResponse
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.journey_row.*

class JourneyItem(private val journeyResponse: JourneyResponse) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            tv_title.text = journeyResponse.journey?.title
            tv_difficulty.text = journeyResponse.journey?.level
        }
    }

    override fun getLayout() = R.layout.journey_row
}
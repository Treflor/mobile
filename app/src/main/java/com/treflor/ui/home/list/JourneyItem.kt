package com.treflor.ui.home.list

import com.treflor.R
import com.treflor.models.Journey
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.journey_row.*

class JourneyItem(val journey:Journey) :Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            tv_title.text = journey.title
            tv_difficulty.text = journey.level
        }
    }

    override fun getLayout() = R.layout.journey_row
}
package com.treflor.ui.home.list

import com.treflor.R
import com.treflor.data.db.entities.journey.JourneyListEntity
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.journey_row.*

class JourneyItem(private val journeyListEntity: JourneyListEntity) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            tv_title.text = journeyListEntity.journey?.title
            tv_difficulty.text = journeyListEntity.journey?.level
        }
    }

    override fun getLayout() = R.layout.journey_row
}
package com.treflor.internal.eventexcecutor

import android.content.Intent

interface ActivityNavigation {
    fun startActivityForResult(intent: Intent?, requestCode: Int)
    fun navigateUp(): Boolean
}
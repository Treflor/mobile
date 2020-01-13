package com.treflor.internal.eventexcecutor

import android.content.Intent
import android.view.View
import com.google.android.material.snackbar.Snackbar

interface ActivityNavigation {
    fun startActivityForResult(intent: Intent?, requestCode: Int)
    fun navigateUp(): Boolean
    fun showSnackBar(s: String)
}
package com.treflor.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.treflor.R
import kotlinx.android.synthetic.main.sign_up_fragment.*
import java.util.*

class SignUpFragment : Fragment() {


    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_up_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        // gender spinner adapter
        gender_spinner.adapter = object : ArrayAdapter<String>(
            this.context!!, android.R.layout.simple_spinner_dropdown_item,
            listOf("Male", "Female", "Gender")
        ) {
            override fun getCount(): Int = 2
        }
        gender_spinner.setSelection(2)

        // set max date to today by device time
        birthday_picker.maxDate = Calendar.getInstance().timeInMillis

    }

}

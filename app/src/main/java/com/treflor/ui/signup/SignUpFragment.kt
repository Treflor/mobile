package com.treflor.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.google.android.material.snackbar.Snackbar
import com.treflor.R
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.internal.drawableToBase64
import com.treflor.internal.eventexcecutor.ActivityNavigation
import com.treflor.internal.getTimeInMillisFromDatePicker
import com.treflor.internal.imageToBase64
import kotlinx.android.synthetic.main.sign_up_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.Calendar

class SignUpFragment : Fragment(), View.OnClickListener, KodeinAware, ActivityNavigation {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: SignUpViewModelFactory by instance()

    private lateinit var request: SignUpRequest
    private lateinit var viewModel: SignUpViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_up_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        request = SignUpRequest(photo = drawableToBase64(resources, R.drawable.profile_pic))
        navController = Navigation.findNavController(view)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SignUpViewModel::class.java)

        viewModel.liveMessageEvent.setEventReceiver(this, this)
        bindUI()
    }

    private fun bindUI() {
        viewModel.signingIn.observe(this, Observer { signingIn ->
            progress_bar.visibility = if (signingIn) View.VISIBLE else View.INVISIBLE
            btn_sign_in.isEnabled = !signingIn
            btn_sign_up.isEnabled = !signingIn
        })

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

        img_profile_picture.setOnClickListener(this)
        btn_sign_in.setOnClickListener(this)
        btn_sign_up.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.img_profile_picture -> {
                ImagePicker.create(this@SignUpFragment)
                    .returnMode(ReturnMode.ALL)
                    .folderMode(true)
                    .toolbarImageTitle("Select a Profile Image")
                    .single()
                    .imageDirectory("treflor")
                    .start()
            }
            R.id.btn_sign_in -> {
                navController.navigateUp()
            }
            R.id.btn_sign_up -> {
                //TODO : validate
                request.email = txt_email.text.toString()
                request.password = txt_password.text.toString()
                request.password2 = txt_password_again.text.toString()
                request.firstName = txt_given_name.text.toString()
                request.lastName = txt_family_name.text.toString()
                request.gender = gender_spinner.selectedItem.toString()
                request.birthday = getTimeInMillisFromDatePicker(birthday_picker)
                viewModel.signUp(request)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val image = ImagePicker.getFirstImageOrNull(data)

//                load image in to imageview
            Glide.with(this@SignUpFragment)
                .load(image.path)
                .centerCrop()
                .into(img_profile_picture)

//            set image in request
            request.photo = imageToBase64(image.path)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun navigateUp(): Boolean {
        navController.navigate(R.id.action_signUpFragment_to_profileFragment)
        return true
    }

    override fun showSnackBar(s: String) {
        if (view != null) Snackbar.make(view!!, s, Snackbar.LENGTH_SHORT).show()
    }

}

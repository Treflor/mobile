package com.treflor.ui.settings


import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation

import com.treflor.R
import com.treflor.data.repository.Repository
import kotlinx.android.synthetic.main.fragment_settings.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val repository: Repository by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {

//        sign out
        tv_sign_out.setOnClickListener {

            val listener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        repository.signOut()
                        dialog?.dismiss()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        dialog.dismiss()
                    }
                }
            }

//            Create Alert box
            AlertDialog.Builder(context!!).setMessage("Sure you want to sign out!")
                .setPositiveButton("Sign out", listener)
                .setNegativeButton("cancel", listener)
                .show()
        }
//        navigate to edit profile
        edit_profile.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.generalSettings))

//        navigate to change password
        security.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.securitySettings))

//        navigate to privacy policy
        privacy.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.privacyPolicyFragment))

//        navigate to about app
        about.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.aboutAppFragment))

//        navigate to terms of services
        terms_of_service.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.termsOfServiceFragment))
    }

}

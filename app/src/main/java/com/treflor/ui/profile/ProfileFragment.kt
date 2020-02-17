package com.treflor.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.treflor.R
import com.treflor.data.remote.response.JourneyResponse
import com.treflor.databinding.ProfileFragmentBinding
import com.treflor.internal.ui.base.TreflorScopedFragment
import com.treflor.models.User
import com.treflor.ui.profile.images.UserImagesFragment
import com.treflor.ui.profile.journeys.UserJourneyFragment
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ProfileFragment : TreflorScopedFragment(), View.OnClickListener, KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ProfileViewModelFactory by instance()

    private lateinit var navController: NavController

    private lateinit var viewModel: ProfileViewModel
    private lateinit var profileFragmentBinding: ProfileFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
        account_icon.setOnClickListener(this)
        profileFragmentBinding =
            ProfileFragmentBinding.bind(view)
        bindUI()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.account_icon -> navController.navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    private fun bindUI() = launch {
        val user = viewModel.user.await()
        user.observe(this@ProfileFragment, Observer {
            profilePicture(it)
            if (it == null) return@Observer
            profileFragmentBinding.user = it

        })
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(UserJourneyFragment(), "TRIPS")
        adapter.addFragment(UserImagesFragment(), "PHOTOS")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    private fun profilePicture(user: User?) {
        account_icon.visibility = if (user == null) View.VISIBLE else View.GONE
        profile_image.visibility = if (user != null) View.VISIBLE else View.GONE
        if (user == null) return
        Glide.with(this@ProfileFragment)
            .load(user.photo)
            .centerCrop()
            .into(profile_image)
    }

    class ViewPagerAdapter(supportFragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(supportFragmentManager) {

        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }
    }

}
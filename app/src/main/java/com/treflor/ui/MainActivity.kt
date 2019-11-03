package com.treflor.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.treflor.R
import com.treflor.ui.camera.CameraFragment
import com.treflor.ui.home.HomeFragment
import com.treflor.ui.journey.JourneyFragment
import com.treflor.ui.menu.MenuFragment
import com.treflor.ui.routes.RoutesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }

    private fun initUi() {
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottom_journey -> {
                    replaceFragment(JourneyFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottom_camera -> {
                    replaceFragment(CameraFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottom_route -> {
                    replaceFragment(RoutesFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottom_menu -> {
                    replaceFragment(MenuFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
        bottomNavigation.selectedItemId = R.id.bottom_home
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(fragmentContainer.id, fragment).commit()
    }
}

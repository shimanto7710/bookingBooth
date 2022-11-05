/*
 *  Created by Shimanto Ahmed on 11/5/22, 3:30 PM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/5/22, 3:23 PM
 *
 */

package com.example.bookingbooth.ui

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bookingbooth.ui.fragments.LandingFragment
import com.rookie.bookingbooth.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        loadHomeFragment()
    }

    private fun loadHomeFragment(){

//        var fragment: Fragment = LandingFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            com.rookie.bookingbooth.R.id.mainFragmentContainer,
            LandingFragment.newInstance()
        )
        transaction.commit()

    /*supportFragmentManager.commit {
            val homeFragment = LandingFragment.newInstance()
            replace(R.id.mainFragmentContainer, homeFragment, getCanonicalName(homeFragment))
            setReorderingAllowed(true)
            addToBackStack(getCanonicalName(homeFragment))
        }*/
    }
}
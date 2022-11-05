/*
 *  Created by Shimanto Ahmed on 11/6/22, 2:13 AM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/6/22, 12:33 AM
 *
 */

package com.example.bookingbooth.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.bookingbooth.pref.SessionManager
import com.rookie.bookingbooth.databinding.ActivitySplashAcivityBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    lateinit var _binding: ActivitySplashAcivityBinding
    private val splashTimeout: Int = 2
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashAcivityBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        sessionManager= SessionManager(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        val timer = object : Thread() {
            override fun run() {
                try {
                    sleep((splashTimeout * 1000).toLong())
                } catch (e: Exception) {
//                    Log.e(TAG, "splashDelay: Sleep Didn't Work properly", e)
                } finally {
                    openActivity(MainActivity::class.java, null)
                }
            }
        }
        timer.start()
    }

    fun initTheme() {
        if (sessionManager.isDarkTheme()) {
//            setTheme(R.style.SplashActivityTheme_Dark)
        }
    }

    fun initView() {
        if (sessionManager.isFirstOpen()) {
//            sessionManager.setLocale("bn")
            sessionManager.setFirstOpenStatus(false)
        }

    }

    fun openActivity(mClass: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, mClass)
        if (bundle != null)
            intent.putExtras(bundle)
        startActivity(intent)
    }
}
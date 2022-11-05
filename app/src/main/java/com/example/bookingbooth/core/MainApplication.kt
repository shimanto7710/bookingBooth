/*
 *  Created by Shimanto Ahmed on 11/5/22, 2:01 PM
 *  Copyright (c) 2021-2022.  All rights reserved.
 *  Last modified: 11/5/22, 1:57 PM
 *
 */

package com.example.bookingbooth.core

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
public class MainApplication : Application(){
    companion object {
        private lateinit var instance: MainApplication
        public fun getContext(): Context {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Contextor.getInstance().init(applicationContext)
    }
}
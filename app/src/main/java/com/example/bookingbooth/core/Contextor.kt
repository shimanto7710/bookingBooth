/*
 *  Created by Shimanto Ahmed on 11/6/22, 2:13 AM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/6/22, 12:33 AM
 *
 */

package com.example.bookingbooth.core

import android.annotation.SuppressLint
import android.content.Context

class Contextor {

    lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var sInstance: Contextor? = null

        @JvmStatic
        fun getInstance(): Contextor {
            if (sInstance == null) {
                sInstance = Contextor()
            }
            return sInstance as Contextor
        }

    }
}
/*
 *  Created by Shimanto Ahmed on 11/5/22, 11:23 PM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/5/22, 1:31 PM
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
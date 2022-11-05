/*
 *  Created by Shimanto Ahmed on 11/6/22, 2:13 AM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/6/22, 12:33 AM
 *
 */

package com.example.bookingbooth.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.example.bookingbooth.pref.SessionManager
import com.rookie.bookingbooth.BuildConfig
import java.util.*

object Utls {

    private var prefHelper: SessionManager? = null
    fun isNetworkConnected(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null)
                for (i in info.indices)
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }

        }
        return false
    }

      fun showToast(msg: String) {
        Toast.makeText(Contextor.getInstance().context, msg, Toast.LENGTH_SHORT).show()
    }

      fun showErrorMessage(msg: String?) {
        if (msg != null) {
            Toast.makeText(Contextor.getInstance().context, msg, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(Contextor.getInstance().context, "Something Wrong", Toast.LENGTH_LONG).show()
        }
    }

    fun isDebugBuild(): Boolean {
        return BuildConfig.DEBUG
    }

    fun getPrefHelper(): SessionManager {
        if (prefHelper == null) {
            prefHelper = SessionManager(Contextor.getInstance().context)
        }
        return prefHelper!!
    }

    fun updateBaseContextLocale(context: Context): Context {
        val language = getPrefHelper().getLocale()
        //val language = "en"
        val locale = Locale(language)
        Locale.setDefault(locale)

        return updateLocale(context, locale)
    }

    fun updateLocale(context: Context, locale: Locale): Context {
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

}
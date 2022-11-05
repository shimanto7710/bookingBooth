/*
 *  Created by Shimanto Ahmed on 11/5/22, 11:23 PM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/5/22, 3:00 PM
 *
 */

package com.example.bookingbooth.network

import android.content.Context
import com.example.bookingbooth.pref.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor(context: Context) : Interceptor {
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}
/*
 *  Created by Shimanto Ahmed on 11/5/22, 11:23 PM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/5/22, 11:06 PM
 *
 */

package com.example.bookingbooth.repositories

import android.util.Log
import com.example.bookingbooth.network.ApiService
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Response
import javax.inject.Inject

class LoginRepositorySql @Inject constructor(
    private val apiService: ApiService
) : LoginRepository {

    override suspend fun makeLoginRequest(loginRequest: String): Response<String> {
        return apiService.login(loginRequest)
    }

    override fun test(loginRequest: String) {
        Log.d("aaa", "api called")
    }
}

class LoginRepositoryFirebase :LoginRepository {
    override suspend fun makeLoginRequest(loginRequest: String): Response<String> {
        TODO("Not yet implemented")
    }

    override fun test(loginRequest: String) {
        Log.d("aaa", "firebase called")
    }
}
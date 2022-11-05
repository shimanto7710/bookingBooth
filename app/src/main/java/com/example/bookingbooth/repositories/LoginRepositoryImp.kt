/*
 *  Created by Shimanto Ahmed on 11/6/22, 2:13 AM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/6/22, 1:45 AM
 *
 */

package com.example.bookingbooth.repositories

import android.util.Log
import com.example.bookingbooth.network.ApiService
import com.example.bookingbooth.network.FirebaseService
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

    override fun test2(loginRequest: String) {
        TODO("Not yet implemented")
    }
}

class LoginRepositoryFirebase() : LoginRepository {
    override suspend fun makeLoginRequest(loginRequest: String): Response<String> {
        TODO("Not yet implemented")
    }

    override fun test(loginRequest: String) {
        Log.d("aaa", "firebase called")

        FirebaseService().saveData()
    }

    override fun test2(loginRequest: String) {
        FirebaseService().getData()
    }
}
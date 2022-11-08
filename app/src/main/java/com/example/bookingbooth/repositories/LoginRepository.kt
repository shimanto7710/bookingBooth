/*
 *  Created by Shimanto Ahmed on 11/6/22, 2:13 AM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/6/22, 1:45 AM
 *
 */

package com.example.bookingbooth.repositories

import com.example.bookingbooth.network.request.UserRequestModel
import retrofit2.Response

interface LoginRepository {
    suspend fun makeLoginRequest(loginRequest: String) : Response<String>
    fun test(loginRequest: String)
    fun test2(loginRequest: String)
    suspend fun signUpWithEmail(email: String, pass: String, userRequestModel: UserRequestModel) : Response<Int>
    suspend fun createUser(userRequestModel: UserRequestModel) : Response<Int>
    suspend fun isUserAlreadyExist(email: String) : Response<UserRequestModel>
}
/*
 *  Created by Shimanto Ahmed on 11/5/22, 11:23 PM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/5/22, 11:06 PM
 *
 */

package com.example.bookingbooth.repositories

import retrofit2.Response

interface LoginRepository {
    suspend fun makeLoginRequest(loginRequest: String) : Response<String>
    fun test(loginRequest: String)
}
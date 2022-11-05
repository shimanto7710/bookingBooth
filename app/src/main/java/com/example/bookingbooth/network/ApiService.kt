/*
 *  Created by Shimanto Ahmed on 11/6/22, 2:13 AM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/6/22, 12:33 AM
 *
 */

package com.example.bookingbooth.network

import retrofit2.Response
import retrofit2.http.*

/**
 * Interface for defining REST request functions
 */
interface ApiService {

    @POST("upg-auth/api/v1/account/login")
    suspend fun login(@Body loginRequest : String) : Response<String>

}
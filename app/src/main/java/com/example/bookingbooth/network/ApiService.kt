/*
 *  Created by Shimanto Ahmed on 11/5/22, 11:23 PM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/5/22, 3:00 PM
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
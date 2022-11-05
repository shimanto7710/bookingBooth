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
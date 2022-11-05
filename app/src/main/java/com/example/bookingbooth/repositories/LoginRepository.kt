package com.example.bookingbooth.repositories

import retrofit2.Response

interface LoginRepository {
    suspend fun makeLoginRequest(loginRequest: String) : Response<String>
    fun test(loginRequest: String)
}
package com.example.bookingbooth.network

import android.content.Context
import com.example.bookingbooth.core.Constants
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.rookie.bookingbooth.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit instance class
 */
class ApiClient {
//    private lateinit var apiService: ApiService

/*    fun getApiService(context: Context): ApiService {
        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {

            val builder = OkHttpClient.Builder()
//            builder.addInterceptor(HostSelectionInterceptor())
            builder.connectTimeout(5, TimeUnit.MINUTES);
            builder.readTimeout(5, TimeUnit.MINUTES);
            builder.writeTimeout(5, TimeUnit.MINUTES);

            if (BuildConfig.DEBUG) {
                builder.addInterceptor( OkHttpProfilerInterceptor() )
            }

            val client = builder.build()
            apiService= Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(ApiService::class.java)

            *//*val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context))
                .build()
            apiService = retrofit.create(ApiService::class.java)*//*
        }

        return apiService
    }

    *//**
     * Initialize OkhttpClient with our interceptor
     *//*
    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }*/

    companion object {
        fun create(context: Context): ApiService {
            val builder = OkHttpClient.Builder()
//            builder.addInterceptor(HostSelectionInterceptor())
            builder.connectTimeout(5, TimeUnit.MINUTES);
            builder.readTimeout(5, TimeUnit.MINUTES);
            builder.writeTimeout(5, TimeUnit.MINUTES);

            if (BuildConfig.DEBUG) {
                builder.addInterceptor( OkHttpProfilerInterceptor() )
            }

            val client = builder.build()
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .client(okhttpClient(context))
                .build()
                .create(ApiService::class.java)
        }

        private fun okhttpClient(context: Context): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .build()
        }
    }

}
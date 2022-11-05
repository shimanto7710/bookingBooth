
package com.example.bookingbooth.di

import com.example.bookingbooth.core.MainApplication
import com.example.bookingbooth.network.ApiClient
import com.example.bookingbooth.network.ApiService
import com.example.bookingbooth.repositories.LoginRepository
import com.example.bookingbooth.repositories.LoginRepositorySql
import com.example.bookingbooth.repositories.LoginRepositoryFirebase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGson() : Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideApiService() : ApiService {
        return ApiClient.create(MainApplication.getContext())
    }

    @Singleton
    @Provides
    @SqlQualifier
    fun provideApiLoginRepository(
            apiService: ApiService
    ) = LoginRepositorySql(apiService) as LoginRepository

    @Singleton
    @Provides
    @FirebaseQualifier
    fun provideFirebaseLoginRepository() = LoginRepositoryFirebase() as LoginRepository

}
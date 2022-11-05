/*
 *  Created by Shimanto Ahmed on 11/5/22, 11:23 PM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/5/22, 11:06 PM
 *
 */


package com.example.bookingbooth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingbooth.di.FirebaseQualifier
import com.example.bookingbooth.repositories.LoginRepository
import com.example.bookingbooth.repositories.LoginRepositorySql
import com.example.bookingbooth.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @FirebaseQualifier private val loginRepository: LoginRepository
) : ViewModel() {

    private val _loginResponse = MutableLiveData<Resource<String>>()

    val loginResponse : LiveData<Resource<String>>
        get() = _loginResponse


    fun login(email: String, password: String, pin: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if(loginRepository is LoginRepositorySql) {
                _loginResponse.postValue(Resource.Loading())
            }
            try {
                /*val response = loginRepository.makeLoginRequest(LoginRequest(email, pin, password))
                val result = response.body()
                val errorResult = response.errorBody()
                if (response.isSuccessful && response.code() == 200 && result != null) {
                    _loginResponse.postValue(Resource.Success(result))
                } else if (result!=null && result.status == Constants.KEY_FAILED) {
                    _loginResponse.postValue(Resource.Error(result.status))
                } else {
                    val jObjError = JSONObject(response.errorBody()!!.charStream().readText())
                    val errorMessage= jObjError.get("message")
                    _loginResponse.postValue(Resource.Error(errorMessage.toString()))
                }*/
            } catch (e: Exception) {
                _loginResponse.postValue(Resource.Error(e.message.toString()))
            }
        }
    }

    fun test(){
        loginRepository.test("")
    }
}
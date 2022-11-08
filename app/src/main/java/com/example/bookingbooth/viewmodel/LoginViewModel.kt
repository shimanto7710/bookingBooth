/*
 *  Created by Shimanto Ahmed on 11/6/22, 2:13 AM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/6/22, 1:45 AM
 *
 */


package com.example.bookingbooth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingbooth.core.Constants
import com.example.bookingbooth.di.FirebaseQualifier
import com.example.bookingbooth.network.request.UserRequestModel
import com.example.bookingbooth.repositories.LoginRepository
import com.example.bookingbooth.repositories.LoginRepositorySql
import com.example.bookingbooth.utils.Resource
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
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

    fun test2(){
        loginRepository.test2("")
    }

    private val _signUpResponse = MutableLiveData<Resource<Int>>()

    val signupResponse : LiveData<Resource<Int>>
        get() = _signUpResponse


    fun signInWithEmail(email: String, password: String, userRequestModel: UserRequestModel) {
        viewModelScope.launch(Dispatchers.IO) {
            if(loginRepository is LoginRepositorySql) {
                _signUpResponse.postValue(Resource.Loading())
            }
            try {
                val response = loginRepository.signUpWithEmail(email=email, pass=password, userRequestModel=userRequestModel)
                val result = response.body()
                val errorResult = response.errorBody()
                if (response.isSuccessful && response.code() == 200 && result != null) {
                    _signUpResponse.postValue(Resource.Success(result))
                } /*else if (result!=null && result.status == Constants.KEY_FAILED) {
                    _signUpResponse.postValue(Resource.Error(result.status))
                }*/ else {
                    val jObjError = JSONObject(response.errorBody()!!.charStream().readText())
                    val errorMessage= jObjError.get("message")
                    _signUpResponse.postValue(Resource.Error(errorMessage.toString()))
                }
            } catch (e: Exception) {
                _signUpResponse.postValue(Resource.Error(e.message.toString()))
            }
        }
    }

    private val _createUserResponse = MutableLiveData<Resource<Int>>()
    val createUserResponse : LiveData<Resource<Int>>
        get() = _createUserResponse

    fun createUser(userRequestModel: UserRequestModel) {
        viewModelScope.launch(Dispatchers.IO) {
            if(loginRepository is LoginRepositorySql) {
                _createUserResponse.postValue(Resource.Loading())
            }
            try {
                val response = loginRepository.createUser(userRequestModel=userRequestModel)
                val result = response.body()
                val errorResult = response.errorBody()
                if (response.isSuccessful && response.code() == 200 && result != null) {
                    _createUserResponse.postValue(Resource.Success(result))
                }/* else if (result!=null && result.status == Constants.KEY_FAILED) {
                    _createUserResponse.postValue(Resource.Error(result.status))
                }*/ else {
                    val jObjError = JSONObject(response.errorBody()!!.charStream().readText())
                    val errorMessage= jObjError.get("message")
                    _createUserResponse.postValue(Resource.Error(errorMessage.toString()))
                }
            } catch (e: Exception) {
                _createUserResponse.postValue(Resource.Error(e.message.toString()))
            }
        }
    }

    private val _isUserExistResponse = MutableLiveData<Resource<UserRequestModel>>()
    val isUserExistResponse : LiveData<Resource<UserRequestModel>>
        get() = _isUserExistResponse

    fun isUserExist(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if(loginRepository is LoginRepositorySql) {
                _isUserExistResponse.postValue(Resource.Loading())
            }
            try {
                val response = loginRepository.isUserAlreadyExist(email=email)
                val result = response.body()
                val errorResult = response.errorBody()
                if (response.isSuccessful && response.code() == 200 && result != null) {
                    _isUserExistResponse.postValue(Resource.Success(result))
                }/* else if (result!=null && result.status == Constants.KEY_FAILED) {
                    _isUserExistResponse.postValue(Resource.Error(result.status))
                }*/ else {
                    val jObjError = JSONObject(response.errorBody()!!.charStream().readText())
                    val errorMessage= jObjError.get("message")
                    _isUserExistResponse.postValue(Resource.Error(errorMessage.toString()))
                }
            } catch (e: Exception) {
                _isUserExistResponse.postValue(Resource.Error(e.message.toString()))
            }
        }
    }
}
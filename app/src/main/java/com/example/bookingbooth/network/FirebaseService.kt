/*
 *  Created by Shimanto Ahmed on 11/6/22, 2:13 AM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/6/22, 2:11 AM
 *
 */

package com.example.bookingbooth.network

import android.util.Log
import android.widget.Toast
import com.example.bookingbooth.core.Contextor
import com.example.bookingbooth.di.FirebaseQualifier
import com.example.bookingbooth.network.request.UserRequestModel
import com.example.bookingbooth.network.response.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import java.util.concurrent.Semaphore


class FirebaseService {
    private val database = FirebaseDatabase.getInstance()
    public fun saveData() {
        var dbRef = database.getReference("users")
        var id = dbRef.push().key
        var model = UserModel(id = id!!, name = "Shimanto")
        dbRef.child(id).setValue(model).addOnCompleteListener {
            Toast.makeText(Contextor.getInstance().context, "Data Inserted", Toast.LENGTH_LONG)
                .show()
        }.addOnFailureListener {
            Toast.makeText(
                Contextor.getInstance().context,
                "Data Insertion failed",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    public fun getData() {
        /*var dbRef = database.getReference("users")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        var model = item.getValue(UserModel::class.java)

                        var data = dbRef.child(model?.id!!)
                        var m = UserModel(model.id, "Ahmed")
                        data.setValue(m)

                        var task = data.removeValue()
                        task.addOnSuccessListener {
                            Toast.makeText(
                                Contextor.getInstance().context,
                                "Data deleted",
                                Toast.LENGTH_LONG
                            )
                        }
                        Log.d("aaa", model.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    Contextor.getInstance().context,
                    "Data can not be get",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })*/
    }

    suspend fun createUserWithEmail(
        email: String,
        pass: String,
        userRequestModel: UserRequestModel
    ): Response<Int> {
        var aResponse = Response.success(404)
        runBlocking {
            var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
            var status = 404
            kotlin.run {
                mAuth!!.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        status = 200
//                        createUser(userRequestModel=userRequestModel)
                    }
                }.addOnFailureListener {
                    if (it.message?.contains("email address is already in use") == true) {
                        status = 403
                    } else if (it.message?.contains("network error") == true) {
                        status = 400
                    }
                }.await()
            }
            launch(coroutineContext) {
                aResponse = Response.success(status)
            }
        }

        return aResponse
    }

    fun createUser(userRequestModel: UserRequestModel): Response<Int> {
        var aResponse = Response.success(404)
        runBlocking {
            var status = 404
            kotlin.run {
                var dbRef = database.getReference("users")
                try {
                    var id = dbRef.push().key
                    userRequestModel.id=id!!
                    dbRef.child(id!!).setValue(userRequestModel).addOnCompleteListener {
                        status = 200
                    }.addOnFailureListener {
                        status = 400
                    }.await()
                } catch (e: Exception) {
                     status = 400
                }
            }

            launch(coroutineContext) {
                aResponse = Response.success(status)
            }
        }

        return aResponse
    }

    suspend fun isUserIsAlreadyExist(email: String) :Response<UserRequestModel> {
        var userRequestModel:UserRequestModel?=UserRequestModel()
        var aResponse = Response.success(userRequestModel)
        // create a java.util.concurrent.Semaphore with 0 initial permits
        var semaphore: Semaphore = Semaphore(0)
        runBlocking{
            kotlin.run {
//                var dbRef = database.getReference("users")
                var dbRef = database.getReference("users").orderByChild("email")
                    .equalTo(email)
                dbRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (item in snapshot.children) {
                                userRequestModel = item.getValue(UserRequestModel::class.java)
                                semaphore.release()
                            }
                        }else{
                            semaphore.release()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        semaphore.release()
                    }
                })
            }

            launch(coroutineContext) {
                semaphore.acquire()
                aResponse = Response.success(userRequestModel)
            }

        }
        return aResponse
    }


}
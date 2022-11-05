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
import com.example.bookingbooth.network.response.UserModel
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


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
        var dbRef = database.getReference("users")

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

        })
    }
}
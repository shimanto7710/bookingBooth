/*
 *  Created by Shimanto Ahmed on 11/5/22, 11:23 PM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/5/22, 2:02 PM
 *
 */



package com.example.bookingbooth.utils

// A generic class that contains data and status about loading this data.
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Empty<T>(message: String? = null, data: T? = null) : Resource<T>(data, message)
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}

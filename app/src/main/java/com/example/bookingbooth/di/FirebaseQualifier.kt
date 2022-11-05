/*
 *  Created by Shimanto Ahmed on 11/5/22, 11:23 PM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/5/22, 11:00 PM
 *
 */

package com.example.bookingbooth.di

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Qualifier

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
annotation class FirebaseQualifier()

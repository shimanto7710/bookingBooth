/*
 *  Created by Shimanto Ahmed on 11/6/22, 2:13 AM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/6/22, 1:38 AM
 *
 */

package com.example.bookingbooth.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.bookingbooth.utils.getCanonicalName
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.rookie.bookingbooth.R
import com.rookie.bookingbooth.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment(), View.OnClickListener {

    private var mVerificationId: String? = null
    private var mAuth: FirebaseAuth? = null

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    var gso: GoogleSignInOptions? = null
    var gsc: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun init() {
        binding.btnBack.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.signUpWithGoogleLayout.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()

        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso!!)

        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (acct != null) {
            loadSignUpFragment()
//            navigateToSecondActivity()
        }


    }

    private fun loadSignUpFragment() {
        requireActivity().supportFragmentManager.commit {
            val homeFragment = SignUpFragment.newInstance()
            replace(R.id.mainFragmentContainer, homeFragment, getCanonicalName(homeFragment))
            setReorderingAllowed(true)
            addToBackStack(getCanonicalName(homeFragment))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {}
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
//                signInWithEmail()
                checkSignInWithEmail()
//                signIn()
                /*PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+88 01686352645", // Phone number to verify
                    60, // Timeout duration
                    java.util.concurrent.TimeUnit.SECONDS, // Unit of timeout
                    requireActivity(), // Activity (for callback binding)
                    callbacks
                )*/
//                loadSignUpFragment()
            }
            R.id.btnBack -> {
                requireActivity().onBackPressed()
            }
            R.id.signUpWithGoogleLayout -> {

            }
        }
    }

    private fun signInWithEmail(){
        mAuth=FirebaseAuth.getInstance()

        mAuth!!.createUserWithEmailAndPassword("shimanto7710@gmail.com", "12345678").addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("aaa", "user registered successfully")
                loadSignUpFragment()
            }else{
                Log.d("aaa", "user registration failed")
            }
        }
    }

    private fun checkSignInWithEmail(){
        mAuth!!.signInWithEmailAndPassword("shimanto7710@gmail.com","12345678").addOnCompleteListener{
            if(it.isSuccessful){
                Log.d("aaa", "Logged In")
                Toast.makeText(requireContext(), "Logged In successful", Toast.LENGTH_SHORT)
                    .show()
                loadSignUpFragment()
            }else{
                Log.d("aaa", "user LoggedIn failed")
            }
        }
    }


    private fun signIn(){
        val signInIntent = gsc!!.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                loadSignUpFragment()
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
                Log.d("aaa", e.message.toString())
                Log.d("aaa", e.cause.toString())
            }
        }
    }


    private val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    verifyVerificationCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                mVerificationId = s
                //mResendToken = forceResendingToken
            }
        }

    private fun verifyVerificationCode(code: String) {
        //creating the credential
        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)

        //signing the user
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential).addOnCompleteListener(
            requireActivity(),
            OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    //verification successful we will start the profile activity
                    /*Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
                    val i = Intent(requireContext(), SuccessActivity::class.java)
                    startActivity(i)*/
                } else {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_LONG).show()
                }
            })
    }


}
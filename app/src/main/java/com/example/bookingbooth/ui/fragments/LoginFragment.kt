/*
 *  Created by Shimanto Ahmed on 11/6/22, 2:13 AM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/6/22, 1:38 AM
 *
 */

package com.example.bookingbooth.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
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
import com.google.firebase.auth.*
import com.rookie.bookingbooth.R
import com.rookie.bookingbooth.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment(), View.OnClickListener {

//    private var mVerificationId: String? = null
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
        binding.tvJoinNow.setOnClickListener(this)

        binding.warningImageOfEmail.visibility=View.GONE
        binding.warningLabelOfEmail.visibility=View.GONE
        binding.warningLabelOfPass.visibility=View.GONE
        binding.warningImageOfPass.visibility=View.GONE


        mAuth = FirebaseAuth.getInstance()

        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso!!)


    }

    private fun loadSignUpFragment() {
        requireActivity().supportFragmentManager.commit {
            val homeFragment = SignUpFragment.newInstance()
            replace(R.id.mainFragmentContainer, homeFragment, getCanonicalName(homeFragment))
            setReorderingAllowed(true)
            addToBackStack(getCanonicalName(homeFragment))
        }
    }

    private fun loadHomeFragment() {
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
                if (isValidate()) {
                    // default login check
                    checkSignInWithEmail(
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString()
                    )
                }

                // login with phone number
                /*PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+88 01686352645", // Phone number to verify
                    60, // Timeout duration
                    java.util.concurrent.TimeUnit.SECONDS, // Unit of timeout
                    requireActivity(), // Activity (for callback binding)
                    callbacks
                )*/
            }
            R.id.btnBack -> {
                requireActivity().onBackPressed()
            }
            R.id.signUpWithGoogleLayout -> {
                signInWithGmail()
            }
            R.id.tvJoinNow -> {
                loadSignUpFragment()
            }
        }
    }

    private fun isValidate():Boolean{
        var isEmailValid=true
        var isPassValid=true
        if (!isValidEmail(binding.etEmail.text.toString()) || binding.etEmail.text.toString().isNullOrEmpty()) {
            binding.warningImageOfEmail.visibility = View.VISIBLE
            binding.warningLabelOfEmail.visibility = View.VISIBLE
            isEmailValid=false
        }else{
            binding.warningImageOfEmail.visibility = View.GONE
            binding.warningLabelOfEmail.visibility = View.GONE
            isEmailValid=true
        }

        if (binding.etPassword.text.toString().isNullOrEmpty() || binding.etPassword.text.toString().length < 6) {
            binding.warningLabelOfPass.visibility = View.VISIBLE
            binding.warningImageOfPass.visibility = View.VISIBLE
            isPassValid=false
        }else{
            binding.warningLabelOfPass.visibility = View.GONE
            binding.warningImageOfPass.visibility = View.GONE
            isPassValid=true
        }

        if (!isPassValid || !isEmailValid){
            return false
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkSignInWithEmail(email: String, pass: String) {
        mAuth!!.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Successfully Logged In", Toast.LENGTH_LONG).show()
                loadSignUpFragment()
            } else {
                Toast.makeText(requireContext(), "User Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signInWithGmail() {
        val signInIntent = gsc!!.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                loadHomeFragment()
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    /*private val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
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
                    *//*Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
                    val i = Intent(requireContext(), SuccessActivity::class.java)
                    startActivity(i)*//*
                } else {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_LONG).show()
                }
            })
    }*/


}
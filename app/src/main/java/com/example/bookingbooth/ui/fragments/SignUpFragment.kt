/*
 *  Created by Shimanto Ahmed on 11/6/22, 2:13 AM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/6/22, 1:38 AM
 *
 */

package com.example.bookingbooth.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.rookie.bookingbooth.R
import com.rookie.bookingbooth.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class SignUpFragment : Fragment(), View.OnClickListener {

    private var mAuth: FirebaseAuth? = null
    var gso: GoogleSignInOptions? = null
    var gsc: GoogleSignInClient? = null

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun init() {
        binding.btnBack.setOnClickListener(this)

        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso!!)

        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (acct != null) {
            val personName = acct.displayName
            val personEmail = acct.email
            Log.d("aaa", "name $personName")
            Log.d("aaa", "email $personEmail")
        }
    }

    fun signOut() {
        gsc!!.signOut().addOnCompleteListener {
            requireActivity().onBackPressed()
        }
    }

    private fun signInWithEmail(email: String, pass: String) {
        mAuth = FirebaseAuth.getInstance()

        mAuth!!.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("aaa", "user registered successfully")
//                loadSignUpFragment()
            } else {
                Log.d("aaa", "user registration failed")
            }
        }
    }

    private fun isValidate():Boolean{
        var isEmailValid=true
        var isPassValid=true
        var isUserName=true
        var isPhoneNumber=true
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

        if (binding.etUserName.text.toString().isNullOrEmpty() || binding.etUserName.text.toString().length < 6) {
            binding.warningLabelOfUserName.visibility = View.VISIBLE
            binding.warningImageOfUserName.visibility = View.VISIBLE
            isUserName=false
        }else{
            binding.warningLabelOfUserName.visibility = View.GONE
            binding.warningImageOfUserName.visibility = View.GONE
            isUserName=true
        }

        if (binding.etPhone.text.toString().isNullOrEmpty() || binding.etPhone.text.toString().length < 6) {
            binding.warningLabelOfPhone.visibility = View.VISIBLE
            binding.warningImageOfPhone.visibility = View.VISIBLE
            isPhoneNumber=false
        }else{
            binding.warningLabelOfPhone.visibility = View.GONE
            binding.warningImageOfPhone.visibility = View.GONE
            isPhoneNumber=true
        }

        if (!isPassValid || !isEmailValid || !isUserName || !isPhoneNumber) {
            return false
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SignUpFragment().apply {}
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBack -> {
                requireActivity().onBackPressed()
            }
        }
    }
}
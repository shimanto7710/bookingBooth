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
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.bookingbooth.network.request.UserRequestModel
import com.example.bookingbooth.utils.*
import com.example.bookingbooth.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.rookie.bookingbooth.R
import com.rookie.bookingbooth.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Matcher
import java.util.regex.Pattern

@AndroidEntryPoint
class SignUpFragment : Fragment(), View.OnClickListener {

    private var mAuth: FirebaseAuth? = null
    var gso: GoogleSignInOptions? = null
    var gsc: GoogleSignInClient? = null
    private val loginViewModel by viewModels<LoginViewModel>()
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
        binding.btnSignup.setOnClickListener(this)
        binding.tvLoginNow.setOnClickListener(this)
        binding.tvLoginWithGoogle.setOnClickListener(this)

        binding.warningImageOfEmail.visibility = View.GONE
        binding.warningLabelOfEmail.visibility = View.GONE

        binding.warningLabelOfPass.visibility = View.GONE
        binding.warningImageOfPass.visibility = View.GONE

        binding.warningLabelOfUserName.visibility = View.GONE
        binding.warningImageOfUserName.visibility = View.GONE

        binding.warningLabelOfPhone.visibility = View.GONE
        binding.warningImageOfPhone.visibility = View.GONE

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
        signOut()
        observeSignUpResponse()
        observeCreateUserResponse()
    }

    fun signOut() {
        gsc!!.signOut().addOnCompleteListener {
//            requireActivity().onBackPressed()
        }
    }

    private fun observeSignUpResponse() {
        loginViewModel.signupResponse.observe(viewLifecycleOwner) { t ->
            when (t) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    if (t.data == 200) {
                        var userRequestModel = UserRequestModel(
                            userName = binding.etUserName.text.toString(),
                            email = binding.etEmail.text.toString(),
                            passport = binding.etPassword.text.toString(),
                            phone = binding.etPhone.text.toString()
                        )
                        loginViewModel.createUser(userRequestModel = userRequestModel)
                        sendVerificationEmail()
                    } else if (t.data == 403) {
                        "User Already Exist".toast(requireContext(), Toast.LENGTH_LONG)
                    } else {
                        "Internet Error".toast(requireContext(), Toast.LENGTH_LONG)
                    }
                }
                is Resource.Error -> {
                    if (t.message?.contains("email address is already in use") == true) {
                        "User Already Exist".toast(requireContext(), Toast.LENGTH_LONG)
                    } else if (t.message?.contains("network error") == true) {
                        "Internet Error".toast(requireContext(), Toast.LENGTH_LONG)
                    } else {
                        "Internet Error".toast(requireContext(), Toast.LENGTH_LONG)
                    }
                    hideProgressBar()
//                    "${t.message}".toast(requireContext(), Toast.LENGTH_LONG)
                }
                is Resource.Empty -> {
                    //do nothing
                }
            }
        }
    }

    private fun observeCreateUserResponse() {
        loginViewModel.createUserResponse.observe(viewLifecycleOwner) { t ->
            when (t) {
                is Resource.Loading -> {
//                    showProgressBar()
                }
                is Resource.Success -> {
//                    hideProgressBar()
                    if (t.data == 200) {
                        "User Created".toast(requireContext(), Toast.LENGTH_LONG)
                    } else {
                        "Failed to create a user".toast(requireContext(), Toast.LENGTH_LONG)
                    }
                }
                is Resource.Error -> {
//                    hideProgressBar()
                    "${t.message}".toast(requireContext(), Toast.LENGTH_LONG)
                }
                is Resource.Empty -> {
                    //do nothing
                }
            }
        }
    }

    private fun sendVerificationEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // email sent
                    // after email is sent just logout the user and finish this activity
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(
                        requireContext(),
                        "Verification email has been sent, Please check!",
                        Toast.LENGTH_LONG
                    ).show()
                    requireActivity().supportFragmentManager.commit {
                        val homeFragment = LoginFragment.newInstance()
                        replace(
                            R.id.mainFragmentContainer,
                            homeFragment,
                            getCanonicalName(homeFragment)
                        )
                        setReorderingAllowed(true)
                        addToBackStack(getCanonicalName(homeFragment))
                    }
                } else {
                    // email not sent, so display message and restart the activity or do whatever you wish to do
                    Toast.makeText(
                        requireContext(),
                        "Verification email unable to send, try again letter",
                        Toast.LENGTH_LONG
                    ).show()
                    //restart this activity
                    requireActivity().supportFragmentManager.commit {
                        val homeFragment = LoginFragment.newInstance()
                        replace(
                            R.id.mainFragmentContainer,
                            homeFragment,
                            getCanonicalName(homeFragment)
                        )
                        setReorderingAllowed(true)
                        addToBackStack(getCanonicalName(homeFragment))
                    }
                }
            }
    }

    private fun isValidate(): Boolean {
        var isEmailValid = true
        var isPassValid = true
        var isUserName = true
        var isPhoneNumber = true
        if (!isValidEmail(binding.etEmail.text.toString()) || binding.etEmail.text.toString()
                .isNullOrEmpty()
        ) {
            binding.warningImageOfEmail.visibility = View.VISIBLE
            binding.warningLabelOfEmail.visibility = View.VISIBLE
            isEmailValid = false
        } else {
            binding.warningImageOfEmail.visibility = View.GONE
            binding.warningLabelOfEmail.visibility = View.GONE
            isEmailValid = true
        }

        if (binding.etPassword.text.toString()
                .isNullOrEmpty() || binding.etPassword.text.toString().length < 6
        ) {
            binding.warningLabelOfPass.visibility = View.VISIBLE
            binding.warningImageOfPass.visibility = View.VISIBLE
            isPassValid = false
        } else {
            binding.warningLabelOfPass.visibility = View.GONE
            binding.warningImageOfPass.visibility = View.GONE
            isPassValid = true
        }

        if (binding.etUserName.text.toString()
                .isNullOrEmpty() || binding.etUserName.text.toString().length < 6
        ) {
            binding.warningLabelOfUserName.visibility = View.VISIBLE
            binding.warningImageOfUserName.visibility = View.VISIBLE
            isUserName = false
        } else {
            binding.warningLabelOfUserName.visibility = View.GONE
            binding.warningImageOfUserName.visibility = View.GONE
            isUserName = true
        }

        if (binding.etPhone.text.toString()
                .isNullOrEmpty() || !isValidPhoneNumber(binding.etPhone.text.toString())
        ) {
            binding.warningLabelOfPhone.visibility = View.VISIBLE
            binding.warningImageOfPhone.visibility = View.VISIBLE
            isPhoneNumber = false
        } else {
            binding.warningLabelOfPhone.visibility = View.GONE
            binding.warningImageOfPhone.visibility = View.GONE
            isPhoneNumber = true
        }

        if (!isPassValid || !isEmailValid || !isUserName || !isPhoneNumber) {
            return false
        }

        return true
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
//        val pattern = Regex("^(?:(?:\\+|00)88|01)?\\d{11}\$", RegexOption.IGNORE_CASE)
        var pattern = "^(?:(?:\\+|00)88|01)?\\d{11}\$"
        val p: Pattern = Pattern.compile(pattern!!)
        var matcher: Matcher = p.matcher(phone)
        return matcher.find()
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showProgressBar() {
        binding.progressLayout.root.visibility = View.VISIBLE
        requireActivity().window.disableUserInteraction()
    }

    private fun hideProgressBar() {
        if (binding.progressLayout.root.isVisible) {
            binding.progressLayout.root.visibility = View.GONE
            requireActivity().window.enableUserInteraction()
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
                val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
                if (acct != null) {
                    val personName = acct.displayName
                    val personEmail = acct.email
                    Log.d("aaa", "name $personName")
                    Log.d("aaa", "email $personEmail")
                    var userRequestModel = UserRequestModel(
                        userName = personName?:"Not Found",
                        email = personEmail?:"Not Found",
                        passport = "Not Found",
                        phone = "Not Found"
                    )
                    loginViewModel.createUser(userRequestModel = userRequestModel)
                    loadHomeFragment()
                }
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun loadHomeFragment() {
        requireActivity().supportFragmentManager.commit {
            val homeFragment = HomeFragment.newInstance()
            replace(R.id.mainFragmentContainer, homeFragment, getCanonicalName(homeFragment))
            setReorderingAllowed(true)
            addToBackStack(getCanonicalName(homeFragment))
        }
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
            R.id.tvLoginNow -> {
                requireActivity().supportFragmentManager.commit {
                    val homeFragment = LoginFragment.newInstance()
                    replace(
                        R.id.mainFragmentContainer,
                        homeFragment,
                        getCanonicalName(homeFragment)
                    )
                    setReorderingAllowed(true)
                    addToBackStack(getCanonicalName(homeFragment))
                }
            }
            R.id.tvLoginWithGoogle -> {
                signInWithGmail()
            }
            R.id.btnSignup -> {
                if (isValidate()) {
                    var userRequestModel = UserRequestModel(
                        userName = binding.etUserName.text.toString(),
                        email = binding.etEmail.text.toString(),
                        passport = binding.etPassword.text.toString(),
                        phone = binding.etPhone.text.toString()
                    )
                    loginViewModel.signInWithEmail(
                        email = binding.etEmail.text.toString(),
                        password = binding.etPassword.text.toString(),
                        userRequestModel = userRequestModel
                    )
                }
            }
        }
    }
}
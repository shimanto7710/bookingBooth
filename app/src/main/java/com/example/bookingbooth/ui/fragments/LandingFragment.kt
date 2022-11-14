/*
 *  Created by Shimanto Ahmed on 11/6/22, 2:13 AM
 *  Copyright (c) 2022.  All rights reserved.
 *  Last modified: 11/6/22, 1:45 AM
 *
 */

package com.example.bookingbooth.ui.fragments

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.bookingbooth.pref.SessionManager
import com.example.bookingbooth.utils.getCanonicalName
import com.example.bookingbooth.viewmodel.LoginViewModel
import com.google.firebase.database.DatabaseReference
import com.rookie.bookingbooth.R
import com.rookie.bookingbooth.databinding.FragmentLandingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLandingBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLandingBinding.inflate(layoutInflater)
        binding.btnLogin.setOnClickListener(this)
        binding.tvSkip.setOnClickListener(this)
        /*loadLoginFragment()*/
        return binding.root
    }

    private fun loadLoginFragment() {
        requireActivity().supportFragmentManager.commit {
            val homeFragment = LoginFragment.newInstance()
            add(R.id.mainFragmentContainer, homeFragment, getCanonicalName(homeFragment))
            setReorderingAllowed(true)
            addToBackStack(getCanonicalName(homeFragment))
        }
    }

    private fun loadHomeFragment() {
        var sessionManager= SessionManager(requireContext())
        sessionManager.setLoginStatus(true)
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
            LandingFragment().apply {}
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnLogin -> {
//                loginViewModel.test()
//                loginViewModel.test2()
                loadLoginFragment()
            }
            R.id.tvSkip -> {
//                loginViewModel.test()
//                loginViewModel.test2()
                loadHomeFragment()
            }
        }
    }


}
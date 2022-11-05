package com.example.bookingbooth.pref

import android.content.Context
import android.content.SharedPreferences
import com.example.bookingbooth.core.Constants
import com.rookie.bookingbooth.R

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.prefName), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun putString(key: String?, value: String?) {
        val ed: SharedPreferences.Editor = prefs.edit()
        ed.putString(key, value)
        ed.apply()
    }

    fun putBoolean(key: String?, value: Boolean) {
        val ed: SharedPreferences.Editor = prefs.edit()
        ed.putBoolean(key, value)
        ed.apply()
    }

    fun putInt(key: String?, value: Int) {
        val ed: SharedPreferences.Editor = prefs.edit()
        ed.putInt(key, value)
        ed.apply()
    }

    fun getSelectedTheme(): String? {
        return prefs.getString(PreConstant.pfTheme.name, Constants.THEME_LIGHT)
    }

    fun setSelectedTheme(theme: String) {
        putString(PreConstant.pfTheme.name, theme)
    }

    fun isDarkTheme(): Boolean {
        return getSelectedTheme() == Constants.THEME_DARK
    }

    fun setLocale(locale: String) {
        putString(PreConstant.locale.name, locale)
    }

    fun getLocale(): String? {
        return prefs.getString(PreConstant.locale.name, "en")
    }

    fun isLoggedIn(): Boolean {
        //return prefUtils.getBoolean(PreConstant.isLoggedIn.name, false)
        return isAccessTokenExist()
    }

    fun setLoginStatus(isLoggedIn: Boolean) {
        putBoolean(PreConstant.isLoggedIn.name, isLoggedIn)

    }

    fun isAccessTokenExist(): Boolean {
        val token = getAccessToken()
        if (token.isNullOrEmpty()) return false

        return true
    }

    fun getAccessToken(): String? {
        return prefs.getString(PreConstant.userAccessToken.name, null)
    }

    fun setAccessToken(token: String) {
        putString(PreConstant.userAccessToken.name, token)
    }

    fun isFirstOpen(): Boolean {
        return prefs.getBoolean(PreConstant.isFirstOpen.name, true)
    }

    fun setFirstOpenStatus(isFirstOpen: Boolean) {
        putBoolean(PreConstant.isFirstOpen.name, isFirstOpen)
    }
}
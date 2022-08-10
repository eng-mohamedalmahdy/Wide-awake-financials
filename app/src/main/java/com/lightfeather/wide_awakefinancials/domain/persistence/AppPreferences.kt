package com.lightfeather.wide_awakefinancials.domain.persistence

import android.content.Context
import android.content.SharedPreferences
import java.util.*

private const val FILE_NAME = "PRAYERS_PREF"
private const val CURRENCY = "CURRENCY"
private const val DARK_MODE_ENABLED = "DARK_MODE_ENABLED"

class AppPreferences(val context: Context) {

    private val preferences by lazy {
        context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun setDarkModeEnabled(enabled: Boolean) = preferences.edit().putBoolean(DARK_MODE_ENABLED, enabled).apply()


    fun isDarkModeEnabled() = preferences.getBoolean(DARK_MODE_ENABLED, true)


    fun setCurrency(currency: String) {
        preferences.edit().putString(CURRENCY, currency).apply()
    }

    fun getCurrency() = preferences.getString(CURRENCY, "EGP")
}
package com.lightfeather.wide_awakefinancials.domain.persistence

import android.content.Context
import android.content.SharedPreferences


class AppPreferences(context: Context) {
    private val FILE_NAME = "PRAYERS_PREF"
    private val CITY_KEY = "CITY_PREF"
    private val COUNTRY_KEY = "COUNTRY_PREF"
    private val METHOD_KEY = "METHOD_PREF"
    private val LAT_KEY = "LAT_PREF"
    private val LNG_KEY = "LNG_PREF"
    private val TASBEEH_KEY = "TASBEEH"
    private val DARK_MODE_ENABLED = "DARK_MODE_ENABLED"
    private var preferences: SharedPreferences? = null

    init {
        preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    }

    fun setDarkModeEnabled(enabled: Boolean) {
        preferences!!.edit().putBoolean(DARK_MODE_ENABLED, enabled).apply()
    }

    fun isDarkModeEnabled(): Boolean {
        return preferences!!.getBoolean(DARK_MODE_ENABLED, true)
    }
}
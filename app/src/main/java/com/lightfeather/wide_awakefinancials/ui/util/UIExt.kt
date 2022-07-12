package com.lightfeather.wide_awakefinancials.ui.util

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import com.airbnb.lottie.LottieAnimationView
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.ui.util.AnimationEndListener
import java.util.*

fun Activity.startLoading() {
    findViewById<View>(R.id.loading_view).visibility = View.VISIBLE
}

fun Activity.stopLoading() {
    findViewById<View>(R.id.loading_view).visibility = View.GONE
}

fun LottieAnimationView.addEndAnimatorListener(listener: AnimationEndListener) =
    addAnimatorListener(listener)

fun Activity.showBottomNavigation() {
    findViewById<View>(R.id.bottom_navigation).visibility = View.VISIBLE
}

fun Activity.hideBottomNavigation() {
    findViewById<View>(R.id.bottom_navigation).visibility = View.GONE
}

fun FragmentActivity.getCurrentLanguage(): Locale? {
    return (this as? LocalizationActivity)?.getCurrentLanguage()
}

fun FragmentActivity.setCurrentLanguage(language: String) {
    (this as? LocalizationActivity)?.setLanguage(language)
}

fun Context.setNightMode(enabled: Boolean) {
    val mode =
        if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
    AppCompatDelegate.setDefaultNightMode(mode)
}
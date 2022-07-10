package com.lightfeather.wide_awakefinancials.ui

import android.app.Activity
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.ui.util.AnimationEndListener

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
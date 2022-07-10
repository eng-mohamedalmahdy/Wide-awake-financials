package com.lightfeather.wide_awakefinancials.ui.util

import android.animation.Animator

fun interface AnimationEndListener : Animator.AnimatorListener {

    override fun onAnimationStart(p0: Animator?) {
    }

    override fun onAnimationCancel(p0: Animator?) {
    }

    override fun onAnimationRepeat(p0: Animator?) {
    }
}
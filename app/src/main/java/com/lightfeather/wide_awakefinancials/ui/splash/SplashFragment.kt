package com.lightfeather.wide_awakefinancials.ui.splash

import android.animation.Animator
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.databinding.FragmentSplashBinding
import com.lightfeather.wide_awakefinancials.ui.addEndAnimatorListener
import com.lightfeather.wide_awakefinancials.ui.showBottomNavigation


class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater)
        with(binding) {
            splashAnimation.addEndAnimatorListener {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
                (requireActivity() as Activity).showBottomNavigation()
            }
        }
        return binding.root
    }

}
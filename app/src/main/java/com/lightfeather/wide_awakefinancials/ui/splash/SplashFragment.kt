package com.lightfeather.wide_awakefinancials.ui.splash

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.lightfeather.wide_awakefinancials.databinding.FragmentSplashBinding
import com.lightfeather.wide_awakefinancials.ui.util.addEndAnimatorListener
import com.lightfeather.wide_awakefinancials.ui.util.showBottomNavigation


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
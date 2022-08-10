package com.lightfeather.wide_awakefinancials.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lightfeather.wide_awakefinancials.databinding.FragmentSettingsBinding
import com.lightfeather.wide_awakefinancials.domain.persistence.AppPreferences
import com.lightfeather.wide_awakefinancials.ui.util.getCurrentLanguage
import com.lightfeather.wide_awakefinancials.ui.util.setCurrentLanguage
import com.lightfeather.wide_awakefinancials.ui.util.setNightMode
import com.lightfeather.wide_awakefinancials.ui.util.showBottomNavigation


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private var firstTime = true
    private val prefs by lazy { AppPreferences(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            privacyPolicyMenuItem.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToPrivacyPolicyFragment())
            }

            rateUsMenuItem.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=${requireActivity().packageName}")
                    )
                )
            }
        }

        setupSpinner()
    }

    private fun setupSpinner() {
        with(binding) {
            currency.setText(prefs.getCurrency())
            currency.addTextChangedListener { prefs.setCurrency(it.toString()) }
            val currentLanguage = requireActivity().getCurrentLanguage()
            if (currentLanguage?.language.equals("ar", true)) {
                languageSelector.setSelection(0)
            } else {
                languageSelector.setSelection(1)
            }
            languageSelector.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
                    if (!firstTime) {
                        if (i == 0) {
                            requireActivity().setCurrentLanguage("ar")
                        } else if (i == 1) {
                            requireActivity().setCurrentLanguage("en")

                        }
                    }
                    firstTime = false
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
            val prefs = AppPreferences(requireContext())
            switchingDarkMode.isChecked = prefs.isDarkModeEnabled()
            switchingDarkMode.setOnCheckedChangeListener { _, isChecked ->
                requireActivity().setNightMode(isChecked)
                prefs.setDarkModeEnabled(isChecked)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().showBottomNavigation()
    }

}
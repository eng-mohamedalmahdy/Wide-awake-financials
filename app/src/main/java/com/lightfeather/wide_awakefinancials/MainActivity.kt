package com.lightfeather.wide_awakefinancials

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : LocalizationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(this, R.id.nav_host_fragment)
        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigation)
        setupWithNavController(bottomNavigationView, navController)

    }


}
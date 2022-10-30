package com.maouni92.runtracker.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maouni92.runtracker.BuildConfig
import com.maouni92.runtracker.R
import com.maouni92.runtracker.data.RunDAO
import com.maouni92.runtracker.helper.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    lateinit var navHostFragment:Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val bottomNavBar:BottomNavigationView = findViewById(R.id.bottom_navigation_view)
       navigateToTrackingFragmentIfNeeded(intent)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.runFragment, R.id.statisticsFragment, R.id.settingsFragment
            )
        )

        navController.addOnDestinationChangedListener{ _ , destination, _->
            bottomNavBar.visibility  =  when(destination.id){
                R.id.runFragment, R.id.statisticsFragment, R.id.settingsFragment ->
                     View.VISIBLE
                else -> View.GONE
            }
        }
        bottomNavBar.setupWithNavController(navController)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if(intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
           findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_tracking_fragment)
        }
    }
}
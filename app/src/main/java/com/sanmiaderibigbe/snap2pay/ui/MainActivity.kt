package com.sanmiaderibigbe.snap2pay.ui

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.sanmiaderibigbe.snap2pay.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)
                as NavHostFragment

        val navController = host.navController

        bottomNav = findViewById<BottomNavigationView>(R.id.menu_bottom_nav)

        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment), drawerLayout)

        setupBottomNavMenu(navController)

        //TODO fix drawer layout setup.
        setupNavigationMenu(navController)

        setupActionBar(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                destination.id.toString()
            }

            when (destination.id) {
                R.id.homeFragment -> showBottomNav()

                R.id.transactionFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }

    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {

        val navController = findNavController(R.id.my_nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupBottomNavMenu(navController: NavController) {

        bottomNav?.setupWithNavController(navController)
    }


    private fun setupNavigationMenu(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)
    }

    private fun showBottomNav() {
        bottomNav.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        bottomNav.visibility = View.GONE

    }
}




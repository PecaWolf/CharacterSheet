package com.pecawolf.charactersheet

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pecawolf.charactersheet.databinding.ActivityMainBinding
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.MainViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel { parametersOf() }
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }
    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_inventory,
                R.id.navigation_skills,
                R.id.navigation_other
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.isLoading.reObserve(this) { isLoading ->
            binding.progress.isVisible = isLoading
            invalidateOptionsMenu()
        }
    }

    override fun onSupportNavigateUp() =
        navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        menu?.let {
            for (i in 0 until it.size()) {
                it.getItem(i).isVisible = viewModel.isLoading.value != true
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_refresh -> viewModel.onRefresh()
            else -> return false
        }
        return true
    }
}
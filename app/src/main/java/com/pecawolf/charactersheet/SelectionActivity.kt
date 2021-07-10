package com.pecawolf.charactersheet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

class SelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        setupActionBarWithNavController(
            findNavController(R.id.nav_host_fragment),
            AppBarConfiguration(setOf(R.id.navigation_choose_character))
        )
    }
}
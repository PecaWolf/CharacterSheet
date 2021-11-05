package com.pecawolf.charactersheet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController

class SelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ResourcesCompat.getColor(resources, R.color.background, null)
        setContentView(R.layout.activity_selection)

        intent.extras?.let {
            findNavController(R.id.nav_host_fragment).navigate(
                R.id.navigation_species_selection
            )
        }
    }

    companion object {
        const val EXTRA_DESTINATION = "EXTRA_DESTINATION"
        const val DESTINATION_CREATE_CHARACTER = "DESTINATION_CREATE_CHARACTER"
    }
}
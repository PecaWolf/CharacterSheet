package com.pecawolf.charactersheet.ui

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pecawolf.charactersheet.R
import com.pecawolf.presentation.viewmodel.BaseViewModel
import timber.log.Timber

abstract class FabMenuFragment<VIEWMODEL : BaseViewModel, BINDING : ViewBinding> :
    BaseFragment<VIEWMODEL, BINDING>() {

    private var isFabMenuExtended: Boolean = false

    abstract fun getMenuFab(): FloatingActionButton
    abstract fun getMenuFabItems(): List<View>

    override fun onViewCreated(fab: View, savedInstanceState: Bundle?) {
        super.onViewCreated(fab, savedInstanceState)

        getMenuFab().setOnClickListener {
            isFabMenuExtended = !isFabMenuExtended
            Timber.d("onMenuFabChange(): $isFabMenuExtended")
            if (isFabMenuExtended) {
                val translationUnit = resources.getDimension(R.dimen.bar_height)

                getMenuFabItems()
                    .asReversed()
                    .forEachIndexed { index, fab ->
                        fab.animate()
                            .setDuration(MENU_ANIMATION_DURATION)
                            .alpha(1f)
                            .translationY(-1 * (index + 1) * translationUnit)
                            .start()
                    }
                getMenuFab().animate()
                    .rotation(-5f * 45f)
                    .setDuration(MENU_ANIMATION_DURATION)
                    .start()
            } else {
                getMenuFabItems().forEach { fab ->
                    fab.animate()
                        .setDuration(MENU_ANIMATION_DURATION)
                        .alpha(0f)
                        .translationY(0f)
                        .start()
                }
                getMenuFab().animate()
                    .rotation(0f)
                    .setDuration(MENU_ANIMATION_DURATION)
                    .start()
            }
        }
    }

    companion object {
        private const val MENU_ANIMATION_DURATION = 500L
    }
}
package cz.pecawolf.charactersheet.ui.inventory

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cz.pecawolf.charactersheet.presentation.InventoryViewModel
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class InventoryFragment : Fragment() {

    private val viewModel: InventoryViewModel by injectVM()
}
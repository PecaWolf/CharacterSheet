package cz.pecawolf.charactersheet.ui.inventory

import cz.pecawolf.charactersheet.R
import cz.pecawolf.charactersheet.databinding.FragmentInventoryBinding
import cz.pecawolf.charactersheet.presentation.InventoryViewModel
import cz.pecawolf.charactersheet.ui.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class InventoryFragment : BaseFragment<InventoryViewModel, FragmentInventoryBinding>() {
    override fun getLayoutResource() = R.layout.fragment_inventory

    override fun createViewModel() = injectVM<InventoryViewModel>().value

    override fun bindView(binding: FragmentInventoryBinding) {
        binding.vm = viewModel
    }

    override fun observeViewModel() {

    }
}
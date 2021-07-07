package cz.pecawolf.charactersheet.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import cz.pecawolf.charactersheet.databinding.FragmentInventoryBinding
import cz.pecawolf.charactersheet.presentation.InventoryViewModel
import cz.pecawolf.charactersheet.ui.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class InventoryFragment : BaseFragment<InventoryViewModel, FragmentInventoryBinding>() {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentInventoryBinding.inflate(inflater, container, false)

    override fun createViewModel() = injectVM<InventoryViewModel>().value

    override fun bindView(binding: FragmentInventoryBinding, viewModel: InventoryViewModel) {
//        viewModel.isLoadoutCombat
//        viewModel.equipment.primary.name
//        viewModel.equipment.secondary.name
//        viewModel.equipment.tertiary.name
//        viewModel.equipment.clothes.name
//        viewModel.equipment.armor.name
//        viewModel.money
    }

    override fun observeViewModel(
        binding: FragmentInventoryBinding,
        viewModel: InventoryViewModel
    ) {

    }
}
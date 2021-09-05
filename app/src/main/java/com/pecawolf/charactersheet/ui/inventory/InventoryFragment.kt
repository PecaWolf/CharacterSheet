package com.pecawolf.charactersheet.ui.inventory

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentInventoryBinding
import com.pecawolf.charactersheet.ext.formatAmount
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.InventoryViewModel
import com.pecawolf.presentation.viewmodel.main.InventoryViewModel.Destination
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class InventoryFragment : BaseFragment<InventoryViewModel, FragmentInventoryBinding>() {

    private val backpackAdapter: InventoryAdapter by lazy {
        InventoryAdapter(viewModel::onItemDetailClicked)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentInventoryBinding.inflate(inflater, container, false)

    override fun createViewModel() = viewModel<InventoryViewModel> { parametersOf() }.value

    override fun bindView(binding: FragmentInventoryBinding, viewModel: InventoryViewModel) {
        binding.inventoryRecycler.apply {
            adapter = backpackAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.inventoryMoneyClicker.setOnClickListener { viewModel.onMoneyClicked() }
        binding.inventoryAddItemFab.setOnClickListener { viewModel.onAddItemClicked() }
    }

    override fun observeViewModel(
        binding: FragmentInventoryBinding,
        viewModel: InventoryViewModel
    ) {
        viewModel.money.reObserve(this) { money ->
            binding.inventoryMoneyValue.text = money.formatAmount()
        }

        viewModel.backpack.reObserve(this) { items ->
            backpackAdapter.items = items
        }

        viewModel.navigateTo.reObserve(this) { destination ->
            when (destination) {
                is Destination.NewItem -> findNavController().navigate(
                    InventoryFragmentDirections.actionInventoryToNewItemStep1()
                )
                is Destination.ItemDetail -> findNavController().navigate(
                    InventoryFragmentDirections.actionInventoryToItemDetail(destination.itemId)
                )
                is Destination.EditMoney -> dialogHelper.showTextInputDialog(
                    getString(R.string.money_edit_title),
                    getString(R.string.money_edit_message),
                    InputType.TYPE_CLASS_NUMBER,
                    1,
                    "${destination.money}",
                    getString(R.string.money_edit_hint),
                    getString(R.string.generic_continue)
                ) { dialog, money ->
                    viewModel.onMoneyConfirmed(money.toInt())
                    dialog.cancel()
                }
            }
        }
    }
}
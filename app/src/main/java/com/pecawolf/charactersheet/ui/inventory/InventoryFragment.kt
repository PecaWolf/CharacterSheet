package com.pecawolf.charactersheet.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.common.formatAmount
import com.pecawolf.charactersheet.databinding.FragmentInventoryBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.InventoryViewModel
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class InventoryFragment : BaseFragment<InventoryViewModel, FragmentInventoryBinding>() {

    private val backpackAdapter: InventoryAdapter by lazy {
        InventoryAdapter(viewModel::onItemEdit)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentInventoryBinding.inflate(inflater, container, false)

    override fun createViewModel() = injectVM<InventoryViewModel>().value

    override fun bindView(binding: FragmentInventoryBinding, viewModel: InventoryViewModel) {
        binding.backpackRecycler.apply {
            adapter = backpackAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.addItemButton.setOnClickListener { viewModel.onAddItem() }
    }

    override fun observeViewModel(
        binding: FragmentInventoryBinding,
        viewModel: InventoryViewModel
    ) {
        viewModel.money.reObserve(this) { money ->
            binding.moneyValue.text = money.formatAmount()
        }

        viewModel.backpack.reObserve(this) { items ->
            backpackAdapter.items = items
        }

        viewModel.navigateTo.reObserve(this) { destination ->
            when (destination) {
                is InventoryViewModel.Destination.ItemDetail -> findNavController().navigate(
                    InventoryFragmentDirections.actionInventoryToItemDetail(destination.itemId)
                )
                is InventoryViewModel.Destination.NewItem -> findNavController().navigate(
                    InventoryFragmentDirections.actionInventoryToNewItemStep1()
                )
            }
        }
    }
}
package com.pecawolf.charactersheet.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.common.formatAmount
import com.pecawolf.charactersheet.databinding.FragmentInventoryBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.InventoryViewModel
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class InventoryFragment : BaseFragment<InventoryViewModel, FragmentInventoryBinding>() {

    private val backpackAdapter: InventoryAdapter by lazy {
        InventoryAdapter(true, viewModel::onItemEdit, viewModel::onItemSwitch)
    }

    private val storageAdapter: InventoryAdapter by lazy {
        InventoryAdapter(false, viewModel::onItemEdit, viewModel::onItemSwitch)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentInventoryBinding.inflate(inflater, container, false)

    override fun createViewModel() = injectVM<InventoryViewModel>().value

    override fun bindView(binding: FragmentInventoryBinding, viewModel: InventoryViewModel) {
        binding.backpackRecycler.apply {
            adapter = backpackAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.storageRecycler.apply {
            adapter = storageAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
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

        viewModel.storage.reObserve(this) { items ->
            storageAdapter.items = items
        }
    }
}
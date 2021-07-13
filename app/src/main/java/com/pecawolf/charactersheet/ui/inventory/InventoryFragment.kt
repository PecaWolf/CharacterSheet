package com.pecawolf.charactersheet.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pecawolf.charactersheet.common.formatAmount
import com.pecawolf.charactersheet.databinding.FragmentInventoryBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.InventoryViewModel
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class InventoryFragment : BaseFragment<InventoryViewModel, FragmentInventoryBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentInventoryBinding.inflate(inflater, container, false)

    override fun createViewModel() = injectVM<InventoryViewModel>().value

    override fun bindView(binding: FragmentInventoryBinding, viewModel: InventoryViewModel) {

    }

    override fun observeViewModel(
        binding: FragmentInventoryBinding,
        viewModel: InventoryViewModel
    ) {
        viewModel.money.reObserve(this) { money ->
            binding.moneyValue.text = money.formatAmount()
        }

        viewModel.backpack.reObserve(this) { inventory ->

        }

        viewModel.storage.reObserve(this) { inventory ->

        }
    }
}
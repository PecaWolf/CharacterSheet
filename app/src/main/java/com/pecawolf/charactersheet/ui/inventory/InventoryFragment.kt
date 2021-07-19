package com.pecawolf.charactersheet.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.common.formatAmount
import com.pecawolf.charactersheet.databinding.FragmentInventoryBinding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.model.Item
import com.pecawolf.presentation.SimpleSelectionItem
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.InventoryViewModel
import com.pecawolf.presentation.viewmodel.main.InventoryViewModel.Destination
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class InventoryFragment : BaseFragment<InventoryViewModel, FragmentInventoryBinding>() {

    private val backpackAdapter: InventoryAdapter by lazy {
        InventoryAdapter(viewModel::onItemEdit, viewModel::onItemEquip)
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
                is Destination.NewItem -> findNavController().navigate(
                    InventoryFragmentDirections.actionInventoryToNewItemStep1()
                )
                is Destination.ItemDetail -> findNavController().navigate(
                    InventoryFragmentDirections.actionInventoryToItemDetail(destination.itemId)
                )
                is Destination.EquipDialog -> showEquipItemDialog(destination.item)
                is Destination.UnequipDialog -> {
                    dialogHelper.showTwoChoiceDialog(
                        getString(R.string.item_unequip_slot_title),
                        getString(
                            R.string.item_unequip_slot_description,
                            destination.item.name,
                            destination.slot.getLocalizedName(requireContext())
                        ),
                        getString(R.string.generic_continue)
                    ) { viewModel.onUnequipItemConfirmed(destination.slot) }
                }
            }
        }
    }

    private fun showEquipItemDialog(item: Item) {
        val items = when (item) {
            is Item.Armor.Clothing -> listOf(Item.Slot.CLOTHING, Item.Slot.ARMOR)
            is Item.Armor -> listOf(Item.Slot.ARMOR)
//            is Item.Weapon.Grenade -> listOf(Item.Slot.GRENADE)
            is Item.Weapon -> listOf(Item.Slot.PRIMARY, Item.Slot.SECONDARY, Item.Slot.TERTIARY)
            else -> throw IllegalArgumentException("This should not happen")
        }
            .map { SimpleSelectionItem(it.getLocalizedName(requireContext()), false, it) }
        dialogHelper.showListChoiceDialog(
            getString(R.string.item_equip_slot_selection_description, item.name),
            false,
            items
        ) { list: List<Item.Slot> -> viewModel.onEquipSlotSelected(item.itemId, list.first()) }
    }
}
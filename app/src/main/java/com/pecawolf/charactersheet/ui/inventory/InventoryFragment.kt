package com.pecawolf.charactersheet.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentInventoryBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.presentation.InventoryViewModel
import com.pecawolf.presentation.extensions.reObserve
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

        binding.loadoutCombat.setOnClickListener { viewModel.onLoadoutCombatClicked() }
        binding.loadoutSocial.setOnClickListener { viewModel.onLoadoutSocialClicked() }
        binding.loadoutTravel.setOnClickListener { viewModel.onLoadoutTravelClicked() }
    }

    override fun observeViewModel(
        binding: FragmentInventoryBinding,
        viewModel: InventoryViewModel
    ) {
        viewModel.loadoutType.reObserve(this) { type ->
            binding.loadoutCombat.isActive =
                type == com.pecawolf.model.Equipment.Item.LoadoutType.COMBAT
            binding.loadoutSocial.isActive =
                type == com.pecawolf.model.Equipment.Item.LoadoutType.SOCIAL
            binding.loadoutTravel.isActive =
                type == com.pecawolf.model.Equipment.Item.LoadoutType.TRAVEL
        }

        viewModel.isPrimaryAllowed.reObserve(this) { isAllowed ->
            val alpha = if (isAllowed) 1f else ResourcesCompat.getFloat(
                resources,
                R.dimen.inactive_item_alpha
            )
            binding.primaryWeaponLabel.alpha = alpha
            binding.primaryWeaponValue.alpha = alpha
        }
        viewModel.isSecondaryAllowed.reObserve(this) { isAllowed ->
            val alpha = if (isAllowed) 1f else ResourcesCompat.getFloat(
                resources,
                R.dimen.inactive_item_alpha
            )
            binding.secondaryWeaponLabel.alpha = alpha
            binding.secondaryWeaponValue.alpha = alpha
        }
        viewModel.isTertiaryAllowed.reObserve(this) { isAllowed ->
            val alpha = if (isAllowed) 1f else ResourcesCompat.getFloat(
                resources,
                R.dimen.inactive_item_alpha
            )
            binding.tertiaryWeaponLabel.alpha = alpha
            binding.tertiaryWeaponValue.alpha = alpha
        }
        viewModel.isClothingAllowed.reObserve(this) { isAllowed ->
            val alpha = if (isAllowed) 1f else ResourcesCompat.getFloat(
                resources,
                R.dimen.inactive_item_alpha
            )
            binding.civilianClothesLabel.alpha = alpha
            binding.civilianClothesValue.alpha = alpha
        }
        viewModel.isArmorAllowed.reObserve(this) { isAllowed ->
            val alpha = if (isAllowed) 1f else ResourcesCompat.getFloat(
                resources,
                R.dimen.inactive_item_alpha
            )
            binding.combatGearLabel.alpha = alpha
            binding.combatGearValue.alpha = alpha
        }
        viewModel.equipment.reObserve(this) { equipment ->
            binding.primaryWeaponValue.text = equipment.primary.name
            binding.secondaryWeaponValue.text = equipment.secondary.name
            binding.tertiaryWeaponValue.text = equipment.tertiary.name
            binding.civilianClothesValue.text = equipment.clothes.name
            binding.combatGearValue.text = equipment.armor.name
        }
        viewModel.money.reObserve(this) {
            binding.moneyValue.text = it
        }
    }
}
package com.pecawolf.charactersheet.ui.loadout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.pecawolf.charactersheet.BuildConfig
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentLoadoutBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.model.Item
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.LoadoutViewModel
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class LoadoutFragment : BaseFragment<LoadoutViewModel, FragmentLoadoutBinding>() {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLoadoutBinding.inflate(inflater, container, false)

    override fun createViewModel() = injectVM<LoadoutViewModel>().value

    override fun bindView(binding: FragmentLoadoutBinding, viewModel: LoadoutViewModel) {
        binding.loadoutCombatCheckbox.setOnClickListener { viewModel.onLoadoutCombatClicked() }
        binding.loadoutSocialCheckbox.setOnClickListener { viewModel.onLoadoutSocialClicked() }
        binding.loadoutTravelCheckbox.setOnClickListener { viewModel.onLoadoutTravelClicked() }
    }

    override fun observeViewModel(
        binding: FragmentLoadoutBinding,
        viewModel: LoadoutViewModel
    ) {
        viewModel.loadoutType.reObserve(this) { type ->
            binding.loadoutCombatCheckbox.isChecked = type == Item.LoadoutType.COMBAT
            binding.loadoutSocialCheckbox.isChecked = type == Item.LoadoutType.SOCIAL
            binding.loadoutTravelCheckbox.isChecked = type == Item.LoadoutType.TRAVEL
        }

        viewModel.isPrimaryAllowed.reObserve(this) { isAllowed ->
            val alpha = if (isAllowed) 1f else ResourcesCompat.getFloat(
                resources,
                R.dimen.inactive_item_alpha
            )
            binding.loadoutWeaponPrimaryLabel.alpha = alpha
            binding.loadoutWeaponPrimaryValue.alpha = alpha
        }
        viewModel.isSecondaryAllowed.reObserve(this) { isAllowed ->
            val alpha = if (isAllowed) 1f else ResourcesCompat.getFloat(
                resources,
                R.dimen.inactive_item_alpha
            )
            binding.loadoutWeaponSecondaryLabel.alpha = alpha
            binding.loadoutWeaponSecondaryValue.alpha = alpha
        }
        viewModel.isTertiaryAllowed.reObserve(this) { isAllowed ->
            val alpha = if (isAllowed) 1f else ResourcesCompat.getFloat(
                resources,
                R.dimen.inactive_item_alpha
            )
            binding.loadoutWeaponTertiaryLabel.alpha = alpha
            binding.loadoutWeaponTertiaryValue.alpha = alpha
        }
        viewModel.isClothingAllowed.reObserve(this) { isAllowed ->
            val alpha = if (isAllowed) 1f else ResourcesCompat.getFloat(
                resources,
                R.dimen.inactive_item_alpha
            )
            binding.loadoutClothesLabel.alpha = alpha
            binding.loadoutClothesValue.alpha = alpha
        }
        viewModel.isArmorAllowed.reObserve(this) { isAllowed ->
            val alpha = if (isAllowed) 1f else ResourcesCompat.getFloat(
                resources,
                R.dimen.inactive_item_alpha
            )
            binding.loadoutArmorLabel.alpha = alpha
            binding.loadoutArmorValue.alpha = alpha
        }
        viewModel.inventory.reObserve(this) { inventory ->
            binding.loadoutWeaponPrimaryValue.text = inventory.primary.name
            binding.loadoutWeaponPrimaryId.apply {
                text = String.format("#%06d", inventory.primary.itemId)
                isVisible = BuildConfig.DEBUG && inventory.primary.itemId > 0
            }
            binding.loadoutWeaponSecondaryValue.text = inventory.secondary.name
            binding.loadoutWeaponSecondaryId.apply {
                text = String.format("#%06d", inventory.secondary.itemId)
                isVisible = BuildConfig.DEBUG && inventory.secondary.itemId > 0
            }
            binding.loadoutWeaponTertiaryValue.text = inventory.tertiary.name
            binding.loadoutWeaponTertiaryId.apply {
                text = String.format("#%06d", inventory.tertiary.itemId)
                isVisible = BuildConfig.DEBUG && inventory.tertiary.itemId > 0
            }
            binding.loadoutClothesValue.text = inventory.clothes.name
            binding.loadoutClothesId.apply {
                text = String.format("#%06d", inventory.clothes.itemId)
                isVisible = BuildConfig.DEBUG && inventory.clothes.itemId > 0
            }
            binding.loadoutArmorValue.text = inventory.armor.name
            binding.loadoutArmorId.apply {
                text = String.format("#%06d", inventory.armor.itemId)
                isVisible = BuildConfig.DEBUG && inventory.armor.itemId > 0
            }
        }
    }
}
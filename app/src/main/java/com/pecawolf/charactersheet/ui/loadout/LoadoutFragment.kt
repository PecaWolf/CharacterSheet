package com.pecawolf.charactersheet.ui.loadout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pecawolf.charactersheet.databinding.FragmentLoadoutBinding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.model.Item
import com.pecawolf.model.RollResult
import com.pecawolf.model.Rollable
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.LoadoutViewModel
import com.pecawolf.presentation.viewmodel.main.LoadoutViewModel.Destination
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LoadoutFragment : BaseFragment<LoadoutViewModel, FragmentLoadoutBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLoadoutBinding.inflate(inflater, container, false)

    override fun createViewModel() = viewModel<LoadoutViewModel> { parametersOf() }.value

    override fun bindView(binding: FragmentLoadoutBinding, viewModel: LoadoutViewModel) {
        binding.loadoutCombatCheckbox.setOnClickListener { viewModel.onLoadoutCombatClicked() }
        binding.loadoutSocialCheckbox.setOnClickListener { viewModel.onLoadoutSocialClicked() }
        binding.loadoutTravelCheckbox.setOnClickListener { viewModel.onLoadoutTravelClicked() }

        binding.loadoutOverviewPrimary.apply {
            setOnDetailClickedListener { viewModel.onPrimaryClicked() }
            setOnReloadClickedListener { viewModel.onPrimaryReloadClicked() }
            setOnRollClickedListener { viewModel.onRollClicked(it as Rollable.Skill, binding.loadoutOverviewPrimary.data) }
        }
        binding.loadoutOverviewSecondary.apply {
            setOnDetailClickedListener { viewModel.onSecondaryClicked() }
            setOnReloadClickedListener { viewModel.onSecondaryReloadClicked() }
            setOnRollClickedListener { viewModel.onRollClicked(it as Rollable.Skill, binding.loadoutOverviewSecondary.data) }
        }
        binding.loadoutOverviewTertiary.apply {
            setOnDetailClickedListener { viewModel.onTertiaryClicked() }
            setOnReloadClickedListener { viewModel.onTertiaryReloadClicked() }
            setOnRollClickedListener { viewModel.onRollClicked(it as Rollable.Skill, binding.loadoutOverviewTertiary.data) }
        }
        binding.loadoutOverviewClothes.apply {
            setOnDetailClickedListener { viewModel.onClothesClicked() }
            setOnRollClickedListener { viewModel.onRollClicked(it as Rollable.Skill, binding.loadoutOverviewClothes.data) }
        }
        binding.loadoutOverviewArmor.apply {
            setOnDetailClickedListener { viewModel.onArmorClicked() }
            setOnRollClickedListener { viewModel.onRollClicked(it as Rollable.Skill, binding.loadoutOverviewArmor.data) }
        }
    }

    override fun observeViewModel(
        binding: FragmentLoadoutBinding,
        viewModel: LoadoutViewModel
    ) {
        viewModel.loadoutType.reObserve(this) { type ->
            binding.loadoutCombatCheckbox.isChecked = type == Item.LoadoutType.COMBAT
            binding.loadoutSocialCheckbox.isChecked = type == Item.LoadoutType.SOCIAL
            binding.loadoutTravelCheckbox.isChecked = type == Item.LoadoutType.TRAVEL
            binding.loadoutCurrentValue.text = type.getLocalizedName(requireContext())
        }

        viewModel.isPrimaryAllowed.reObserve(this) { isAllowed ->
            binding.loadoutOverviewPrimary.isActive = isAllowed
        }
        viewModel.isSecondaryAllowed.reObserve(this) { isAllowed ->
            binding.loadoutOverviewSecondary.isActive = isAllowed
        }
        viewModel.isTertiaryAllowed.reObserve(this) { isAllowed ->
            binding.loadoutOverviewTertiary.isActive = isAllowed
        }
        viewModel.isClothesAllowed.reObserve(this) { isAllowed ->
            binding.loadoutOverviewClothes.isActive = isAllowed
        }
        viewModel.isArmorAllowed.reObserve(this) { isAllowed ->
            binding.loadoutOverviewArmor.isActive = isAllowed
        }
        viewModel.primary.reObserve(this) {
            binding.loadoutOverviewPrimary.apply {
                data = it.first
                skill = it.second
            }
        }
        viewModel.secondary.reObserve(this) {
            binding.loadoutOverviewSecondary.apply {
                data = it.first
                skill = it.second
            }
        }
        viewModel.tertiary.reObserve(this) {
            binding.loadoutOverviewTertiary.apply {
                data = it.first
                skill = it.second
            }
        }
        viewModel.armor.reObserve(this) {
            binding.loadoutOverviewArmor.apply {
                data = it.first
                skill = it.second
            }
        }
        viewModel.clothes.reObserve(this) {
            binding.loadoutOverviewClothes.apply {
                data = it.first
                skill = it.second
            }
        }
        viewModel.navigateTo.reObserve(this) {
            when (it) {
                is Destination.RollModifierDialog -> showRollModifierDialog(it.skill, it.item)
                is Destination.RollResultDialog -> showRollResultDialog(it.rollResult)
                is Destination.ItemDetail -> findNavController().navigate(
                    LoadoutFragmentDirections.actionLoadoutToItemDetail(it.itemId)
                )
            }
        }
    }

    private fun showRollModifierDialog(skill: Rollable.Skill, item: Item?) {
        dialogHelper.showRollModifierDialog(
            skill
        ) { dialog, modifier ->
            viewModel.onRollConfirmed(skill, modifier, item)
            dialog.cancel()
        }
    }

    private fun showRollResultDialog(rollResult: RollResult) {
        dialogHelper.showRollResultDialog(rollResult)
    }
}
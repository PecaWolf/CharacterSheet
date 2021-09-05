package com.pecawolf.charactersheet.ui.loadout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.databinding.FragmentLoadoutBinding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.charactersheet.ui.skills.SkillsAdapter
import com.pecawolf.model.Item
import com.pecawolf.model.RollResult
import com.pecawolf.model.Rollable
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.LoadoutViewModel
import com.pecawolf.presentation.viewmodel.main.LoadoutViewModel.Destination
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LoadoutFragment : BaseFragment<LoadoutViewModel, FragmentLoadoutBinding>() {

    private val skillsAdapter: SkillsAdapter by lazy {
        SkillsAdapter({ viewModel.onRollClicked(it as Rollable.Skill) }).apply {
            showGroups = false
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLoadoutBinding.inflate(inflater, container, false)

    override fun createViewModel() = viewModel<LoadoutViewModel> { parametersOf() }.value

    override fun bindView(binding: FragmentLoadoutBinding, viewModel: LoadoutViewModel) {
        binding.loadoutCombatCheckbox.setOnClickListener { viewModel.onLoadoutCombatClicked() }
        binding.loadoutSocialCheckbox.setOnClickListener { viewModel.onLoadoutSocialClicked() }
        binding.loadoutTravelCheckbox.setOnClickListener { viewModel.onLoadoutTravelClicked() }

        binding.loadoutSkillsRecycler.apply {
            adapter = skillsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.loadoutOverviewPrimary.setOnClickListener { viewModel.onPrimaryClicked() }
        binding.loadoutOverviewSecondary.setOnClickListener { viewModel.onSecondaryClicked() }
        binding.loadoutOverviewTertiary.setOnClickListener { viewModel.onTertiaryClicked() }
        binding.loadoutOverviewClothes.setOnClickListener { viewModel.onClothesClicked() }
        binding.loadoutOverviewArmor.setOnClickListener { viewModel.onArmorClicked() }
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
        viewModel.inventory.reObserve(this) { inventory ->
            binding.loadoutOverviewPrimary.setItem(inventory.primary)
            binding.loadoutOverviewSecondary.setItem(inventory.secondary)
            binding.loadoutOverviewTertiary.setItem(inventory.tertiary)
            binding.loadoutOverviewClothes.setItem(inventory.clothes)
            binding.loadoutOverviewArmor.setItem(inventory.armor)
        }
        viewModel.skills.reObserve(this) {
            skillsAdapter.items = listOf(it)
        }
        viewModel.navigateTo.reObserve(this) {
            when (it) {
                is Destination.RollModifierDialog -> showRollModifierDialog(it.skill)
                is Destination.RollResultDialog -> showRollResultDialog(it.rollResult)
                is Destination.ItemDetail -> findNavController().navigate(
                    LoadoutFragmentDirections.actionLoadoutToItemDetail(it.itemId)
                )
            }
        }
    }

    private fun showRollModifierDialog(skill: Rollable.Skill) {
        dialogHelper.showRollModifierDialog(
            skill
        ) { dialog, modifier ->
            viewModel.onRollConfirmed(skill, modifier)
            dialog.cancel()
        }
    }

    private fun showRollResultDialog(rollResult: RollResult) {
        dialogHelper.showRollResultDialog(rollResult)
    }
}
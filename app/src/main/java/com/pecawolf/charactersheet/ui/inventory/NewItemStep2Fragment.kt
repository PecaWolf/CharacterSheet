package com.pecawolf.charactersheet.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentNewItemStep2Binding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.charactersheet.ui.SimpleSelectionAdapter
import com.pecawolf.model.Item
import com.pecawolf.model.Item.Weapon.Wield
import com.pecawolf.presentation.SimpleSelectionItem
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.NewItemStep2ViewModel
import org.koin.core.parameter.parametersOf
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class NewItemStep2Fragment : BaseFragment<NewItemStep2ViewModel, FragmentNewItemStep2Binding>() {

    private val damageTypeAdapter: SimpleSelectionAdapter by lazy {
        SimpleSelectionAdapter {
            viewModel.onDamageTypeSelected(it as Item.DamageType)
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNewItemStep2Binding.inflate(inflater, container, false)

    override fun createViewModel() = injectVM<NewItemStep2ViewModel> {
        NewItemStep2FragmentArgs.fromBundle(requireArguments()).run {
            parametersOf(
                name,
                description,
                Item.ItemType.valueOf(type)
            )
        }
    }.value

    override fun bindView(binding: FragmentNewItemStep2Binding, viewModel: NewItemStep2ViewModel) {
        binding.newItemStep2LoadoutCombat.setOnClickListener { viewModel.loadoutCombatClicked() }
        binding.newItemStep2LoadoutSocial.setOnClickListener { viewModel.loadoutSocialClicked() }
        binding.newItemStep2LoadoutTravel.setOnClickListener { viewModel.loadoutTravelClicked() }
        binding.newItemStep2DamageLight.setOnClickListener { viewModel.damageLightClicked() }
        binding.newItemStep2DamageMedium.setOnClickListener { viewModel.damageMediumClicked() }
        binding.newItemStep2DamageHeavy.setOnClickListener { viewModel.damageHeavyClicked() }
        binding.newItemStep2MagazineSizeInput.doAfterTextChanged { text ->
            viewModel.onMagazineSizeInputChanged(
                text.toString().toIntOrNull() ?: -1
            )
        }
        binding.newItemStep2RateOfFireInput.doAfterTextChanged { text ->
            viewModel.onRateOfFireInputChanged(
                text.toString().toIntOrNull() ?: -1
            )
        }
        binding.newItemStep2DamageTypesRecycler.apply {
            adapter = damageTypeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.newItemStep2WieldOneHanded.setOnClickListener { viewModel.onWieldSelected(Wield.ONE_HANDED) }
        binding.newItemStep2WieldTwoHanded.setOnClickListener { viewModel.onWieldSelected(Wield.TWO_HANDED) }
        binding.newItemStep2WieldMounted.setOnClickListener { viewModel.onWieldSelected(Wield.MOUNTED) }
        binding.newItemStep2ButtonNext.setOnClickListener { viewModel.onNextClicked() }
    }

    override fun observeViewModel(
        binding: FragmentNewItemStep2Binding,
        viewModel: NewItemStep2ViewModel
    ) {
        viewModel.isWieldVisible.reObserve(this) { isVisible ->
            binding.newItemStep2WieldCard.isVisible = isVisible
        }
        viewModel.isDamageTypesVisible.reObserve(this) { isVisible ->
            binding.newItemStep2DamageTypesCard.isVisible = isVisible
            binding.newItemStep2LoadoutsCard.isVisible = isVisible
        }
        viewModel.isAmmoVisible.reObserve(this) { isVisible ->
            binding.newItemStep2AmmoCard.isVisible = isVisible
        }
        viewModel.loadoutCombatChecked.reObserve(this) { isChecked ->
            binding.newItemStep2LoadoutCombat.isChecked = isChecked
        }
        viewModel.loadoutSocialChecked.reObserve(this) { isChecked ->
            binding.newItemStep2LoadoutSocial.isChecked = isChecked
        }
        viewModel.loadoutTravelChecked.reObserve(this) { isChecked ->
            binding.newItemStep2LoadoutTravel.isChecked = isChecked
        }
        viewModel.damageLightChecked.reObserve(this) { isChecked ->
            binding.newItemStep2DamageLight.isChecked = isChecked
        }
        viewModel.damageMediumChecked.reObserve(this) { isChecked ->
            binding.newItemStep2DamageMedium.isChecked = isChecked
        }
        viewModel.damageHeavyChecked.reObserve(this) { isChecked ->
            binding.newItemStep2DamageHeavy.isChecked = isChecked
        }
        viewModel.magazineSize.reObserve(this) {
            binding.newItemStep2MagazineSizeInput.apply {
                if (text.toString() != it) setText(it)
            }
        }
        viewModel.rateOfFire.reObserve(this) {
            binding.newItemStep2RateOfFireInput.apply {
                if (text.toString() != it) setText(it)
            }
        }
        viewModel.damageTypes.reObserve(this) { items ->
            damageTypeAdapter.items = items.map {
                SimpleSelectionItem(
                    it.first.getLocalizedName(requireContext()),
                    it.second,
                    it.first
                )
            }
        }
        viewModel.selectedWield.reObserve(this) {
            binding.newItemStep2WieldOneHanded.isChecked = it == Wield.ONE_HANDED
            binding.newItemStep2WieldTwoHanded.isChecked = it == Wield.TWO_HANDED
            binding.newItemStep2WieldMounted.isChecked = it == Wield.MOUNTED
        }
        viewModel.isWeapon.reObserve(this) {
            binding.newItemStep2DamageHeader.setText(if (it) R.string.new_item_header_damage else R.string.new_item_header_protection)
            binding.newItemStep2DamageTypesHeader.setText(if (it) R.string.new_item_damage_types_header else R.string.new_item_protection_types_header)
        }
        viewModel.isNextEnabled.reObserve(this) { isEnabled ->
            binding.newItemStep2ButtonNext.isEnabled = isEnabled
        }
        viewModel.navigateTo.reObserve(this) {
            when (it) {
                is NewItemStep2ViewModel.Destination.Overview -> findNavController().navigate(
                    NewItemStep2FragmentDirections.actionNewItemStep2ToItemDetail(it.itemId),
                    NavOptions.Builder().setPopUpTo(R.id.navigation_inventory, false).build()
                )
            }
        }
    }
}
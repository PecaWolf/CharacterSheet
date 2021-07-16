package com.pecawolf.charactersheet.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentNewItemStep2Binding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.charactersheet.ui.SimpleSelectionAdapter
import com.pecawolf.model.Item
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
        binding.loadoutCombat.setOnClickListener {
            viewModel.loadoutCombatClicked()
        }
        binding.loadoutSocial.setOnClickListener {
            viewModel.loadoutSocialClicked()
        }
        binding.loadoutTravel.setOnClickListener {
            viewModel.loadoutTravelClicked()
        }
        binding.damageLight.setOnClickListener {
            viewModel.damageLightClicked()
        }
        binding.damageMedium.setOnClickListener {
            viewModel.damageMediumClicked()
        }
        binding.damageHeavy.setOnClickListener {
            viewModel.damageHeavyClicked()
        }
        binding.magazineSizeInput.doOnTextChanged { text, start, before, count ->
            viewModel.onMagazineSizeInputChanged(
                text.toString().toIntOrNull() ?: -1
            )
        }
        binding.rateOfFireInput.doOnTextChanged { text, start, before, count ->
            viewModel.onRateOfFireInputChanged(
                text.toString().toIntOrNull() ?: -1
            )
        }
        binding.damageTypesRecycler.apply {
            adapter = damageTypeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.wieldOneHanded.setOnClickListener {
            viewModel.onWieldSelected(Item.Weapon.Wield.ONE_HANDED)
        }
        binding.wieldTwoHanded.setOnClickListener {
            viewModel.onWieldSelected(Item.Weapon.Wield.TWO_HANDED)
        }
        binding.wieldMounted.setOnClickListener {
            viewModel.onWieldSelected(Item.Weapon.Wield.MOUNTED)
        }
        binding.wieldDrone.setOnClickListener {
            viewModel.onWieldSelected(Item.Weapon.Wield.DRONE)
        }
        binding.buttonNext.setOnClickListener {
            viewModel.onNextClicked()
        }
    }

    override fun observeViewModel(
        binding: FragmentNewItemStep2Binding,
        viewModel: NewItemStep2ViewModel
    ) {
        viewModel.isWieldVisible.reObserve(this) { isVisible ->
            binding.wieldCard.isVisible = isVisible
        }
        viewModel.isDamageTypesVisible.reObserve(this) { isVisible ->
            binding.damageTypesCard.isVisible = isVisible
            binding.allowedLoadoutsCard.isVisible = isVisible
        }
        viewModel.isAmmoVisible.reObserve(this) { isVisible ->
            binding.ammoCard.isVisible = isVisible
        }
        viewModel.loadoutCombatChecked.reObserve(this) { isChecked ->
            binding.loadoutCombat.isChecked = isChecked
        }
        viewModel.loadoutSocialChecked.reObserve(this) { isChecked ->
            binding.loadoutSocial.isChecked = isChecked
        }
        viewModel.loadoutTravelChecked.reObserve(this) { isChecked ->
            binding.loadoutTravel.isChecked = isChecked
        }
        viewModel.damageLightChecked.reObserve(this) { isChecked ->
            binding.damageLight.isChecked = isChecked
        }
        viewModel.damageMediumChecked.reObserve(this) { isChecked ->
            binding.damageMedium.isChecked = isChecked
        }
        viewModel.damageHeavyChecked.reObserve(this) { isChecked ->
            binding.damageHeavy.isChecked = isChecked
        }
        viewModel.magazineSize.reObserve(this) {
            binding.magazineSizeInput.apply {
                if (text.toString() != it) setText(it)
            }
        }
        viewModel.rateOfFire.reObserve(this) {
            binding.rateOfFireInput.apply {
                if (text.toString() != it) setText(it)
            }
        }
        viewModel.damageTypes.reObserve(this) { items ->
            damageTypeAdapter.items = items.map {
                SimpleSelectionItem(
                    getString(it.first.getLocalizedName()),
                    it.second,
                    it.first
                )
            }
        }
        viewModel.selectedWield.reObserve(this) {
            binding.wieldOneHanded.isChecked = it == Item.Weapon.Wield.ONE_HANDED
            binding.wieldTwoHanded.isChecked = it == Item.Weapon.Wield.TWO_HANDED
            binding.wieldMounted.isChecked = it == Item.Weapon.Wield.MOUNTED
            binding.wieldDrone.isChecked = it == Item.Weapon.Wield.DRONE
        }
        viewModel.isWeapon.reObserve(this) {
            binding.damageHeader.text =
                getString(if (it) R.string.new_item_header_damage else R.string.new_item_header_protection)
            binding.damageTypesHeader.text =
                getString(if (it) R.string.new_item_damage_types_header else R.string.new_item_protection_types_header)
        }
        viewModel.isNextEnabled.reObserve(this) { isEnabled ->
            binding.buttonNext.isEnabled = isEnabled
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
package com.pecawolf.charactersheet.ui.inventory

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.BuildConfig
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentItemDetailBinding
import com.pecawolf.charactersheet.ext.formatAmount
import com.pecawolf.charactersheet.ext.getIcon
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.charactersheet.ui.SimpleSelectionAdapter
import com.pecawolf.model.Item
import com.pecawolf.model.Item.Damage
import com.pecawolf.model.Item.DamageType
import com.pecawolf.model.Item.LoadoutType
import com.pecawolf.model.Item.Weapon.Wield
import com.pecawolf.presentation.SimpleSelectionItem
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.ItemDetailViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ItemDetailFragment : BaseFragment<ItemDetailViewModel, FragmentItemDetailBinding>() {

    private val damageTypeAdapter: SimpleSelectionAdapter by lazy {
        SimpleSelectionAdapter {}
    }

    override fun getBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentItemDetailBinding.inflate(inflater, container, false)

    override fun createViewModel() = viewModel<ItemDetailViewModel> {
        ItemDetailFragmentArgs.fromBundle(requireArguments()).run {
            parametersOf(itemId)
        }
    }.value

    override fun bindView(
        binding: FragmentItemDetailBinding,
        viewModel: ItemDetailViewModel
    ) {
        binding.itemDetailItemEdit.setOnClickListener {
            viewModel.onItemEditClicked()
        }
        binding.itemNameEditIcon.setOnClickListener {
            dialogHelper.showTextInputDialog(
                title = getString(R.string.item_edit_name_title),
                message = getString(R.string.item_edit_name_message),
                inputType = InputType.TYPE_CLASS_TEXT,
                lineCount = 1,
                defaultInput = viewModel.item.value?.name,
                hint = getString(R.string.item_edit_name_hint),
                positiveButton = getString(R.string.generic_ok)
            ) { viewModel.onNameChanged(it) }
        }
        binding.itemDescriptionEditIcon.setOnClickListener {
            dialogHelper.showTextInputDialog(
                title = getString(R.string.item_edit_description_title),
                message = getString(R.string.item_edit_description_message),
                inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE,
                lineCount = 5,
                defaultInput = viewModel.item.value?.description,
                hint = getString(R.string.item_edit_description_hint),
                positiveButton = getString(R.string.generic_ok)
            ) { viewModel.onDescriptionChanged(it) }
        }
        binding.itemCountEditIcon.setOnClickListener {
            dialogHelper.showTextInputDialog(
                title = getString(R.string.item_edit_count_title),
                message = getString(R.string.item_edit_count_message),
                inputType = InputType.TYPE_CLASS_NUMBER,
                lineCount = 1,
                defaultInput = viewModel.item.value?.count?.toString(),
                hint = getString(R.string.item_edit_count_hint),
                positiveButton = getString(R.string.generic_ok)
            ) { viewModel.onCountChanged(it.toInt()) }
        }
        binding.itemDetailLoadoutEditIcon.setOnClickListener {
            viewModel.onLoadoutEditClicked()
        }
        binding.itemDetailDamageEditIcon.setOnClickListener {
            viewModel.onDamageEditClicked()
        }
        binding.itemDetailMagazineSizeEditIcon.setOnClickListener {
            dialogHelper.showTextInputDialog(
                title = getString(R.string.item_edit_magazine_size_title),
                message = getString(R.string.item_edit_magazine_size_message),
                inputType = InputType.TYPE_CLASS_NUMBER,
                lineCount = 1,
                defaultInput = (viewModel.item.value as? Item.Weapon.Ranged)?.magazine?.toString(),
                hint = getString(R.string.item_edit_magazine_size_hint),
                positiveButton = getString(R.string.generic_ok)
            ) { viewModel.onMagazineSizeChanged(it.toInt()) }
        }
        binding.itemDetailRateOfFireEditIcon.setOnClickListener {
            dialogHelper.showTextInputDialog(
                title = getString(R.string.item_edit_rate_of_fire_title),
                message = getString(R.string.item_edit_rate_of_fire_message),
                inputType = InputType.TYPE_CLASS_NUMBER,
                lineCount = 1,
                defaultInput = (viewModel.item.value as? Item.Weapon.Ranged)?.rateOfFire?.toString(),
                hint = getString(R.string.item_edit_rate_of_fire_hint),
                positiveButton = getString(R.string.generic_ok)
            ) { viewModel.onRateOfFireChanged(it.toInt()) }
        }
        binding.itemDetailWieldEditIcon.setOnClickListener {
            viewModel.onWieldEditClicked()
        }
        binding.itemDetailDamageTypesEditIcon.setOnClickListener {
            viewModel.onDamageTypesEditClicked()
        }
    }

    override fun observeViewModel(
        binding: FragmentItemDetailBinding,
        viewModel: ItemDetailViewModel
    ) {
        viewModel.isLoading.reObserve(this) {
            binding.itemDetailProgressBar.isVisible = it
            binding.itemDetailContentClicker.apply {
                isEnabled = it
                isVisible = it
            }
        }
        viewModel.isEquippable.reObserve(this) { isEquippable ->
            binding.itemDetailItemEquip.isVisible = isEquippable
        }
        viewModel.isEditingBaseData.reObserve(this) { isEditing ->
            binding.itemNameEditIcon.isVisible = isEditing
            binding.itemDescriptionEditIcon.isVisible = isEditing
        }
        viewModel.isEditingCount.reObserve(this) { isEditing ->
            binding.itemCountEditIcon.isVisible = isEditing
        }

        viewModel.isEditingLoadoutAndDamage.reObserve(this) { isEditing ->
            binding.itemDetailLoadoutEditIcon.isVisible = isEditing
            binding.itemDetailDamageEditIcon.isVisible = isEditing
            binding.itemDetailDamageTypesEditIcon.isVisible = isEditing
        }

        viewModel.isEditingAmmunition.reObserve(this) { isEditing ->
            binding.itemDetailMagazineSizeEditIcon.isVisible = isEditing
            binding.itemDetailRateOfFireEditIcon.isVisible = isEditing
        }

        viewModel.isEditingWield.reObserve(this) { isEditing ->
            binding.itemDetailWieldEditIcon.isVisible = isEditing
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

        viewModel.item.reObserve(this) { item ->
            setupItemDetails(binding, item)
        }

        viewModel.showLoadoutDialog.reObserve(this) { items ->
            dialogHelper.showListChoiceDialog(getString(R.string.multi_choice_header_loadout),
                false,
                items.map {
                    SimpleSelectionItem(
                        it.first.getLocalizedName(requireContext()),
                        it.second,
                        it.first
                    )
                }) { list: List<LoadoutType> -> viewModel.onLoadoutChanged(list) }
        }

        viewModel.showDamageDialog.reObserve(this) { items ->
            dialogHelper.showListChoiceDialog(getString(R.string.multi_choice_header_damage),
                false,
                items.map {
                    SimpleSelectionItem(
                        it.first.getLocalizedName(requireContext()),
                        it.second,
                        it.first
                    )
                }) { list: List<Damage> -> viewModel.onDamageChanged(list.first()) }
        }

        viewModel.showWieldDialog.reObserve(this) { items ->
            dialogHelper.showListChoiceDialog(getString(R.string.multi_choice_header_wield),
                false,
                items.map {
                    SimpleSelectionItem(
                        it.first.getLocalizedName(requireContext()),
                        it.second,
                        it.first
                    )
                }) { list: List<Wield> -> viewModel.onWieldChanged(list.first()) }
        }

        viewModel.showDamageTypesDialog.reObserve(this) { items ->
            dialogHelper.showListChoiceDialog(getString(R.string.multi_choice_header_damage_type),
                false,
                items.map {
                    SimpleSelectionItem(
                        it.first.getLocalizedName(requireContext()),
                        it.second,
                        it.first
                    )
                }) { list: List<DamageType> -> viewModel.onDamageTypesChanged(list) }
        }
    }

    private fun setupItemDetails(
        binding: FragmentItemDetailBinding,
        item: Item
    ) {
        binding.run {
            itemIcon.setImageDrawable(item.getIcon(resources))
            itemName.text = item.name
            itemId.apply {
                isVisible = BuildConfig.DEBUG
                text = String.format("#%06d", item.itemId)
            }
            itemDescription.text = item.description
            itemCount.apply {
                text = resources.getString(R.string.item_count, item.count)
            }

            itemDetailDamageCard.isVisible = item is Item.Weapon || item is Item.Armor
            itemDetailLoadoutsCard.isVisible = item is Item.Weapon || item is Item.Armor
            itemDetailWieldCard.isVisible = item is Item.Weapon
            itemDetailDamageTypesCard.isVisible = item is Item.Weapon || item is Item.Armor
            itemDetailAmmoCard.isVisible = item is Item.Weapon.Ranged

            if (item is Item.Weapon || item is Item.Armor) {
                loadoutCombat.isChecked = item.allowedLoadouts.contains(LoadoutType.COMBAT)
                loadoutSocial.isChecked = item.allowedLoadouts.contains(LoadoutType.SOCIAL)
                loadoutTravel.isChecked = item.allowedLoadouts.contains(LoadoutType.TRAVEL)

                itemDetailDamageTypesRecycler.apply {
                    adapter = damageTypeAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }

                if (item is Item.Weapon) {
                    damageHeavy.isChecked = item.damage == Damage.HEAVY
                    damageMedium.isChecked = item.damage == Damage.MEDIUM
                    damageLight.isChecked = item.damage == Damage.LIGHT

                    itemDetailDamageHeader.text = getString(R.string.new_item_header_damage)
                    itemDetailDamageTypesHeader.text =
                        getString(R.string.new_item_damage_types_header)

                    itemDetailWieldOneHanded.isChecked = item.wield == Wield.ONE_HANDED
                    itemDetailWieldTwoHanded.isChecked = item.wield == Wield.TWO_HANDED
                    itemDetailWieldMounted.isChecked = item.wield == Wield.MOUNTED
                    itemDetailWieldDrone.isChecked = item.wield == Wield.DRONE
                } else {
                    item as Item.Armor
                    damageHeavy.isChecked = item.damage == Damage.HEAVY
                    damageMedium.isChecked = item.damage == Damage.MEDIUM
                    damageLight.isChecked = item.damage == Damage.LIGHT

                    itemDetailDamageHeader.text = getString(R.string.new_item_header_protection)
                    itemDetailDamageTypesHeader.text =
                        getString(R.string.new_item_protection_types_header)
                }
            }

            if (item is Item.Weapon.Ranged) {
                itemDetailMagazineSizeValue.text = item.magazine.formatAmount()
                itemDetailRateOfFireValue.text = item.rateOfFire.formatAmount()
            }
        }
    }
}
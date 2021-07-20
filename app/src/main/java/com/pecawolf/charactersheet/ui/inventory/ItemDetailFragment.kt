package com.pecawolf.charactersheet.ui.inventory

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
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
import com.pecawolf.presentation.viewmodel.ItemDetailViewModel.Destination
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
        binding.itemDetailItemEditFab.setOnClickListener { viewModel.onItemEditClicked() }
        binding.itemDetailItemEquipFab.setOnClickListener { viewModel.onItemEquipClicked() }
        binding.itemDetailItemDeleteFab.setOnClickListener { viewModel.onItemDeleteClicked() }
        binding.itemDetailNameEditIcon.setOnClickListener { viewModel.onNameEditClicked() }
        binding.itemDetailDescriptionEditIcon.setOnClickListener { viewModel.onDescriptionEditClicked() }
        binding.itemDetailCountEditIcon.setOnClickListener { viewModel.onCountEditClicked() }
        binding.itemDetailLoadoutEditIcon.setOnClickListener { viewModel.onLoadoutEditClicked() }
        binding.itemDetailDamageEditIcon.setOnClickListener { viewModel.onDamageEditClicked() }
        binding.itemDetailMagazineSizeEditIcon.setOnClickListener { viewModel.onMagazineSizeEditClicked() }
        binding.itemDetailRateOfFireEditIcon.setOnClickListener { viewModel.onRateOfFireEditClicked() }
        binding.itemDetailWieldEditIcon.setOnClickListener { viewModel.onWieldEditClicked() }
        binding.itemDetailDamageTypesEditIcon.setOnClickListener { viewModel.onDamageTypesEditClicked() }
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
            binding.itemDetailItemEquipFab.isEnabled = isEquippable
        }
        viewModel.isEditingBaseData.reObserve(this) { isEditing ->
            binding.itemDetailNameEditIcon.isVisible = isEditing
            binding.itemDetailDescriptionEditIcon.isVisible = isEditing
        }
        viewModel.isEditingCount.reObserve(this) { isEditing ->
            binding.itemDetailCountEditIcon.isVisible = isEditing
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
            setupItemDetails(item)
        }

        viewModel.slot.reObserve(this) {
            val isEquipped = it.isNotEmpty()

            binding.itemSlot.apply {
                isVisible = isEquipped
                text = it.firstOrNull()?.getLocalizedName(requireContext())
            }
            binding.itemDetailItemEquipFab.labelText = getString(
                if (isEquipped) R.string.item_detail_fab_label_unequip
                else R.string.item_detail_fab_label_equip
            )
        }

        viewModel.navigateTo.reObserve(this) {
            when (it) {
                is Destination.CountDialog -> showCountDialog(it.count)
                is Destination.DamageDialog -> showDamageDialog(it.items)
                is Destination.DamageTypesDialog -> showDamageTypesDialog(it.items)
                is Destination.DeleteConfirmDialog -> showDeleteDialog(it.name)
                is Destination.DescriptionDialog -> showDescriptionDialog(it.description)
                is Destination.EquipConfirmDialog -> showEquipConfirmDialog(
                    it.name,
                    it.allowedSlots
                )
                is Destination.UnequipConfirmDialog -> showUnequipConfirmDialog(it.name, it.slot)
                Destination.Leave -> leaveItemDetail()
                is Destination.LoadoutDialog -> showLoadoutDialog(it.items)
                is Destination.MagazineSizeDialog -> showMagazineSizeDialog(it.magazine)
                is Destination.NameDialog -> showNameDialog(it.name)
                is Destination.RateOfFireDialog -> showRateOfFireDialog(it.rateOfFire)
                is Destination.WieldDialog -> showWieldDialog(it.items)
            }
        }
    }

    private fun setupItemDetails(item: Item) {
        binding.run {
            itemIcon.setImageDrawable(item.getIcon(resources))
            itemDetailName.text = item.name
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
//                    itemDetailWieldDrone.isChecked = item.wield == Wield.DRONE
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

    private fun showCountDialog(count: Int) {
        dialogHelper.showTextInputDialog(
            title = getString(R.string.item_edit_count_title),
            message = getString(R.string.item_edit_count_message),
            inputType = InputType.TYPE_CLASS_NUMBER,
            lineCount = 1,
            defaultInput = count.toString(),
            hint = getString(R.string.item_edit_count_hint),
            positiveButton = getString(R.string.generic_ok)
        ) { dialog, newCount ->
            viewModel.onCountChanged(newCount.toInt())
            dialog.cancel()
        }
    }

    private fun showDamageDialog(items: List<Pair<Damage, Boolean>>) {
        val selectionItems = items.map {
            SimpleSelectionItem(
                it.first.getLocalizedName(requireContext()), it.second, it.first
            )
        }
        dialogHelper.showListChoiceDialog(
            title = getString(R.string.multi_choice_header_damage),
            isSingleChoice = true,
            items = selectionItems
        ) { dialog, list: List<Damage> ->
            viewModel.onDamageChanged(list.first())
            dialog.cancel()
        }
    }

    private fun showDamageTypesDialog(items: List<Pair<DamageType, Boolean>>) {
        val selectionItems = items.map {
            SimpleSelectionItem(
                it.first.getLocalizedName(requireContext()), it.second, it.first
            )
        }
        dialogHelper.showListChoiceDialog(
            title = getString(R.string.multi_choice_header_damage_type),
            isSingleChoice = false,
            items = selectionItems
        ) { dialog, list: List<DamageType> ->
            viewModel.onDamageTypesChanged(list)
            dialog.cancel()
        }
    }

    private fun showDeleteDialog(name: String) {
        dialogHelper.showDeleteItemDialog(name) { dialog, price ->
            viewModel.onItemDeleteConfirmed(price)
            dialog.cancel()
        }
    }

    private fun showDescriptionDialog(description: String) {
        dialogHelper.showTextInputDialog(
            title = getString(R.string.item_edit_description_title),
            message = getString(R.string.item_edit_description_message),
            inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE,
            lineCount = 5,
            defaultInput = description,
            hint = getString(R.string.item_edit_description_hint),
            positiveButton = getString(R.string.generic_ok)
        ) { dialog, newDescription ->
            viewModel.onDescriptionChanged(newDescription)
            dialog.cancel()
        }
    }

    private fun showEquipConfirmDialog(name: String, allowedSlots: List<Item.Slot>) {
        val items = allowedSlots
            .map { SimpleSelectionItem(it.getLocalizedName(requireContext()), false, it) }
        dialogHelper.showListChoiceDialog(
            getString(R.string.item_equip_slot_selection_description, name),
            true,
            items
        ) { dialog, list: List<Item.Slot> ->
            viewModel.onEquipSlotSelected(list.first())
            dialog.cancel()
        }
    }

    private fun showUnequipConfirmDialog(name: String, slot: Item.Slot) {
        dialogHelper.showTwoChoiceDialog(
            getString(R.string.item_unequip_slot_title),
            getString(
                R.string.item_unequip_slot_description,
                name,
                slot.getLocalizedName(requireContext())
            ),
            getString(R.string.generic_continue)
        ) { dialog ->
            viewModel.onUnequipItemConfirmed()
            dialog.cancel()
        }
    }

    private fun leaveItemDetail() {
        findNavController().popBackStack(R.id.navigation_inventory, false)
    }

    private fun showLoadoutDialog(items: List<Pair<LoadoutType, Boolean>>) {
        val selectionItems = items.map {
            SimpleSelectionItem(
                it.first.getLocalizedName(requireContext()), it.second, it.first
            )
        }
        dialogHelper.showListChoiceDialog(
            getString(R.string.multi_choice_header_loadout),
            false,
            selectionItems,
        )
        { dialog, list: List<LoadoutType> ->
            viewModel.onLoadoutChanged(list)
            dialog.cancel()
        }
    }

    private fun showMagazineSizeDialog(magazine: Int) {
        dialogHelper.showTextInputDialog(
            title = getString(R.string.item_edit_magazine_size_title),
            message = getString(R.string.item_edit_magazine_size_message),
            inputType = InputType.TYPE_CLASS_NUMBER,
            lineCount = 1,
            defaultInput = magazine.toString(),
            hint = getString(R.string.item_edit_magazine_size_hint),
            positiveButton = getString(R.string.generic_ok)
        ) { dialog, magazineSize ->
            viewModel.onMagazineSizeChanged(magazineSize.toInt())
            dialog.cancel()
        }
    }

    private fun showNameDialog(name: String) {
        dialogHelper.showTextInputDialog(
            title = getString(R.string.item_edit_name_title),
            message = getString(R.string.item_edit_name_message),
            inputType = InputType.TYPE_CLASS_TEXT,
            lineCount = 1,
            defaultInput = name,
            hint = getString(R.string.item_edit_name_hint),
            positiveButton = getString(R.string.generic_ok)
        ) { dialog, name ->
            viewModel.onNameChanged(name)
            dialog.cancel()
        }
    }

    private fun showRateOfFireDialog(rateOfFire: Int) {
        dialogHelper.showTextInputDialog(
            title = getString(R.string.item_edit_rate_of_fire_title),
            message = getString(R.string.item_edit_rate_of_fire_message),
            inputType = InputType.TYPE_CLASS_NUMBER,
            lineCount = 1,
            defaultInput = rateOfFire.toString(),
            hint = getString(R.string.item_edit_rate_of_fire_hint),
            positiveButton = getString(R.string.generic_ok)
        ) { dialog, rateOfFire ->
            viewModel.onRateOfFireChanged(rateOfFire.toInt())
            dialog.cancel()
        }
    }

    private fun showWieldDialog(items: List<Pair<Wield, Boolean>>) {
        val selectionItems = items.map {
            SimpleSelectionItem(
                it.first.getLocalizedName(requireContext()), it.second, it.first
            )
        }
        dialogHelper.showListChoiceDialog(
            title = getString(R.string.multi_choice_header_wield),
            isSingleChoice = true,
            items = selectionItems
        )
        { dialog, list: List<Wield> ->
            viewModel.onWieldChanged(list.first())
            dialog.cancel()
        }
    }
}
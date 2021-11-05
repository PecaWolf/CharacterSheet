package com.pecawolf.charactersheet.ui.inventory

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.pecawolf.charactersheet.BuildConfig
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentItemDetailBinding
import com.pecawolf.charactersheet.ext.formatAmount
import com.pecawolf.charactersheet.ext.getIcon
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.charactersheet.ui.SimpleSelectionAdapter
import com.pecawolf.charactersheet.ui.view.initialize
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

    private val wieldAdapter: SimpleSelectionAdapter by lazy {
        SimpleSelectionAdapter {}
    }

    override fun getBinding(
        inflater: LayoutInflater, container: ViewGroup?,
    ) = FragmentItemDetailBinding.inflate(inflater, container, false)

    override fun createViewModel() = viewModel<ItemDetailViewModel> {
        ItemDetailFragmentArgs.fromBundle(requireArguments()).run {
            parametersOf(itemId)
        }
    }.value

    override fun bindView(
        binding: FragmentItemDetailBinding,
        viewModel: ItemDetailViewModel,
    ) {
        binding.itemDetailItemEditFab.setOnClickListener { viewModel.onItemEditClicked() }
        binding.itemDetailItemEquipFab.setOnClickListener { viewModel.onItemEquipClicked() }
        binding.itemDetailItemDeleteFab.setOnClickListener { viewModel.onItemDeleteClicked() }

        binding.itemDetailBaseDataCard.itemDetailNameEditIcon.setOnClickListener { viewModel.onNameEditClicked() }
        binding.itemDetailBaseDataCard.itemDetailDescriptionEditIcon.setOnClickListener { viewModel.onDescriptionEditClicked() }
        binding.itemDetailBaseDataCard.itemDetailCountEditIcon.setOnClickListener { viewModel.onCountEditClicked() }

        binding.itemDetailLoadoutsCard.itemDetailLoadoutEditIcon.setOnClickListener { viewModel.onLoadoutEditClicked() }
        binding.itemDetailDamageCard.itemDetailDamageEditIcon.setOnClickListener { viewModel.onDamageEditClicked() }
        binding.itemDetailAmmoCard.itemDetailMagazineSizeEditIcon.setOnClickListener { viewModel.onMagazineSizeEditClicked() }
        binding.itemDetailAmmoCard.itemDetailRateOfFireEditIcon.setOnClickListener { viewModel.onRateOfFireEditClicked() }
        binding.itemDetailAmmoStateCard.itemDetailMagazineStateRefreshIcon.setOnClickListener { viewModel.onMagazineStateRefreshClicked() }
        binding.itemDetailAmmoStateCard.itemDetailMagazineCountEditIcon.setOnClickListener { viewModel.onMagazineCountEditClicked() }
        binding.itemDetailWieldCard.itemDetailWieldEditIcon.setOnClickListener { viewModel.onWieldEditClicked() }
        binding.itemDetailDamageTypesCard.itemDetailDamageTypesEditIcon.setOnClickListener { viewModel.onDamageTypesEditClicked() }
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
            binding.itemDetailItemEditFab.isChecked = isEditing

            binding.itemDetailBaseDataCard.itemDetailNameEditIcon.isVisible = isEditing
            binding.itemDetailBaseDataCard.itemDetailDescriptionEditIcon.isVisible = isEditing
        }
        viewModel.isEditingCount.reObserve(this) { isEditing ->
            binding.itemDetailBaseDataCard.itemDetailCountEditIcon.isVisible = isEditing
        }
        viewModel.isEditingLoadoutAndDamage.reObserve(this) { isEditing ->
            binding.itemDetailLoadoutsCard.itemDetailLoadoutEditIcon.isVisible = isEditing
            binding.itemDetailDamageCard.itemDetailDamageEditIcon.isVisible = isEditing
            binding.itemDetailDamageTypesCard.itemDetailDamageTypesEditIcon.isVisible = isEditing
        }
        viewModel.isEditingAmmunition.reObserve(this) { isEditing ->
            binding.itemDetailAmmoCard.itemDetailMagazineSizeEditIcon.isVisible = isEditing
            binding.itemDetailAmmoCard.itemDetailRateOfFireEditIcon.isVisible = isEditing
            binding.itemDetailAmmoStateCard.itemDetailMagazineStateRefreshIcon.isVisible = isEditing
            binding.itemDetailAmmoStateCard.itemDetailMagazineCountEditIcon.isVisible = isEditing
        }
        viewModel.isEditingWield.reObserve(this) { isEditing ->
            binding.itemDetailWieldCard.itemDetailWieldEditIcon.isVisible = isEditing
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

            binding.itemDetailBaseDataCard.itemDetailSlot.apply {
                isInvisible = !isEquipped
                text = it.firstOrNull()?.getLocalizedName(requireContext())
            }
            binding.itemDetailItemEquipFab.isChecked = isEquipped
        }

        viewModel.navigateTo.reObserve(this) {
            when (it) {
                is Destination.CountDialog -> showCountDialog(it.count)
                is Destination.DamageDialog -> showDamageDialog(it.items)
                is Destination.DamageTypesDialog -> showDamageTypesDialog(it.items)
                is Destination.DeleteConfirmDialog -> showDeleteDialog(it.name)
                is Destination.DescriptionDialog -> showDescriptionDialog(it.description)
                is Destination.EquipConfirmDialog -> showEquipConfirmDialog(it.name, it.slots)
                is Destination.UnequipConfirmDialog -> showUnequipConfirmDialog(it.name, it.slot)
                Destination.Leave -> leaveItemDetail()
                is Destination.LoadoutDialog -> showLoadoutDialog(it.items)
                is Destination.MagazineSizeDialog -> showMagazineSizeDialog(it.magazine)
                is Destination.NameDialog -> showNameDialog(it.name)
                is Destination.RateOfFireDialog -> showRateOfFireDialog(it.rateOfFire)
                is Destination.MagazineCountDialog -> showMagazineCountDialog(it.magazineCount)
                is Destination.WieldDialog -> showWieldDialog(it.items)
            }
        }
    }

    private fun setupItemDetails(item: Item) {
        setupVisibilities(item)
        setupBaseData(item)
        setupDamage(item)
        setupLoadout(item)
        setupCombatGear(item)
    }

    private fun setupVisibilities(item: Item) {
        binding.itemDetailDamageCard.root.isVisible = item is Item.Weapon || item is Item.Armor
        binding.itemDetailLoadoutsCard.root.isVisible = item is Item.Weapon || item is Item.Armor
        binding.itemDetailWieldCard.root.isVisible = item is Item.Weapon
        binding.itemDetailDamageTypesCard.root.isVisible = item is Item.Weapon || item is Item.Armor
        binding.itemDetailAmmoCard.root.isVisible = item is Item.Weapon.Ranged
        binding.itemDetailAmmoStateCard.root.isVisible = item is Item.Weapon.Ranged
    }

    private fun setupBaseData(item: Item) {
        binding.itemDetailBaseDataCard.run {
            itemDetailIcon.setImageDrawable(item.getIcon(resources))
            itemDetailName.text = item.name
            itemDetailId.apply {
                isVisible = BuildConfig.DEBUG
                text = String.format("#%06d", item.itemId)
            }
            itemDetailDescription.text = item.description
            itemDetailCount.apply {
                text = resources.getString(R.string.item_count, item.count)
            }
        }
    }

    private fun setupDamage(item: Item) {
        binding.itemDetailDamageCard.run {
            itemDetailDamageHeavy.isChecked = item.damage == Damage.HEAVY
            itemDetailDamageMedium.isChecked = item.damage == Damage.MEDIUM
            itemDetailDamageLight.isChecked = item.damage == Damage.LIGHT
        }
    }

    private fun setupLoadout(item: Item) {
        binding.itemDetailLoadoutsCard.run {
            itemDetailLoadoutCombat.isChecked = item.allowedLoadouts.contains(LoadoutType.COMBAT)
            itemDetailLoadoutSocial.isChecked = item.allowedLoadouts.contains(LoadoutType.SOCIAL)
            itemDetailLoadoutTravel.isChecked = item.allowedLoadouts.contains(LoadoutType.TRAVEL)
        }
    }

    private fun setupCombatGear(item: Item) {
        binding.run {
            itemDetailDamageTypesCard.itemDetailDamageTypesRecycler.initialize(damageTypeAdapter)

            if (item is Item.Weapon) {
                itemDetailDamageCard.itemDetailDamageHeader.setText(R.string.new_item_header_damage)
                itemDetailDamageTypesCard.itemDetailDamageTypesHeader.setText(R.string.new_item_damage_types_header)

                itemDetailWieldCard.itemDetailWield.text = item.wield.getLocalizedName(requireContext())

                if (item is Item.Weapon.Ranged) {
                    itemDetailAmmoCard.itemDetailMagazineSizeValue.text = item.magazineSize.formatAmount()
                    itemDetailAmmoCard.itemDetailRateOfFireValue.text = item.rateOfFire.formatAmount()

                    itemDetailAmmoStateCard.itemDetailMagazineStateValue.text = item.magazineState.formatAmount()
                    itemDetailAmmoStateCard.itemDetailMagazineCountValue.text = item.magazineCount.formatAmount()
                }
            } else if (item is Item.Armor) {
                itemDetailDamageCard.itemDetailDamageHeader.setText(R.string.new_item_header_protection)
                itemDetailDamageTypesCard.itemDetailDamageTypesHeader.setText(R.string.new_item_protection_types_header)
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
        dialogHelper.showEquipConfirmDialog(name, allowedSlots) { dialog, list: List<Item.Slot> ->
            viewModel.onEquipSlotSelected(list.first())
            dialog.cancel()
        }
    }

    private fun showUnequipConfirmDialog(name: String, slot: Item.Slot) {
        dialogHelper.showUnequipConfirmDialog(name, slot) { dialog ->
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

    private fun showNameDialog(currentName: String) {
        dialogHelper.showTextInputDialog(
            title = getString(R.string.item_edit_name_title),
            message = getString(R.string.item_edit_name_message),
            inputType = InputType.TYPE_CLASS_TEXT,
            lineCount = 1,
            defaultInput = currentName,
            hint = getString(R.string.item_edit_name_hint),
            positiveButton = getString(R.string.generic_ok)
        ) { dialog, name ->
            viewModel.onNameChanged(name)
            dialog.cancel()
        }
    }

    private fun showRateOfFireDialog(currentRateOfFire: Int) {
        dialogHelper.showTextInputDialog(
            title = getString(R.string.item_edit_rate_of_fire_title),
            message = getString(R.string.item_edit_rate_of_fire_message),
            inputType = InputType.TYPE_CLASS_NUMBER,
            lineCount = 1,
            defaultInput = currentRateOfFire.toString(),
            hint = getString(R.string.item_edit_rate_of_fire_hint),
            positiveButton = getString(R.string.generic_ok)
        ) { dialog, rateOfFire ->
            viewModel.onRateOfFireChanged(rateOfFire.toInt())
            dialog.cancel()
        }
    }

    private fun showMagazineCountDialog(currentMagazineCount: Int) {
        dialogHelper.showTextInputDialog(
            title = getString(R.string.item_edit_magazine_count_title),
            message = getString(R.string.item_edit_magazine_count_message),
            inputType = InputType.TYPE_CLASS_NUMBER,
            lineCount = 1,
            defaultInput = currentMagazineCount.toString(),
            hint = getString(R.string.item_edit_magazine_count_hint),
            positiveButton = getString(R.string.generic_ok)
        ) { dialog, magazineCount ->
            viewModel.onMagazineCountChanged(magazineCount.toInt())
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
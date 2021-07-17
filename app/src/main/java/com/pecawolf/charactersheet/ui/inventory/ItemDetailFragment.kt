package com.pecawolf.charactersheet.ui.inventory

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.pecawolf.charactersheet.BuildConfig
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentItemDetailBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.ItemDetailViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ItemDetailFragment : BaseFragment<ItemDetailViewModel, FragmentItemDetailBinding>() {

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
        binding.itemEdit.setOnClickListener {
            viewModel.onItemEditClicked()
        }
        binding.itemName.setOnClickListener {
            showTextInputDialog(
                getString(R.string.item_edit_name_title),
                InputType.TYPE_CLASS_TEXT,
                1,
                getString(R.string.item_edit_name_hint),
                binding.itemName.text.toString(),
                getString(R.string.generic_ok)
            ) { viewModel.onNameChanged(it) }
        }
        binding.itemDescription.setOnClickListener {
            showTextInputDialog(
                getString(R.string.item_edit_description_title),
                InputType.TYPE_CLASS_TEXT,
                5,
                getString(R.string.item_edit_description_hint),
                binding.itemName.text.toString(),
                getString(R.string.generic_ok)
            ) { viewModel.onDescriptionChanged(it) }
        }
        binding.itemCount.setOnClickListener {
            showTextInputDialog(
                getString(R.string.item_edit_count_title),
                InputType.TYPE_CLASS_NUMBER,
                1,
                getString(R.string.item_edit_count_hint),
                binding.itemName.text.toString(),
                getString(R.string.generic_ok)
            ) { viewModel.onCountChanged(it.toInt()) }
        }
    }

    override fun observeViewModel(
        binding: FragmentItemDetailBinding,
        viewModel: ItemDetailViewModel
    ) {
        viewModel.isEditing.reObserve(this) { isEditing ->
            binding.itemName.isEnabled = isEditing
            binding.itemNameEditIcon.isVisible = isEditing
            binding.itemDescription.isEnabled = isEditing
            binding.itemDescriptionEditIcon.isVisible = isEditing
            binding.itemCount.isEnabled = isEditing
            binding.itemCountEditIcon.isVisible = isEditing
        }
        viewModel.item.reObserve(this) { item ->
            binding.itemName.text = item.name
            binding.itemId.apply {
                isVisible = BuildConfig.DEBUG
                text = String.format("#%06d", item.itemId)
            }
            binding.itemDescription.text = item.description
            binding.itemCount.apply {
                text = resources.getString(R.string.item_count, item.count)
            }

//            binding.itemEquip.apply {
//                isVisible = false
//                val isEquipable =
//                    item is Item.Weapon || item is Item.Armor || item is Item.Weapon.Grenade
//                isVisible = isEquipable
//                isEnabled = isEquipable
//                isChecked = slot != null
//                setOnClickListener { viewModel.onItemEquip() }
//            }
//            binding.itemSlot.apply {
//                isVisible = false
//                text = slot?.getLocalizedName()?.let {
//                    resources.getString(it)
//                } ?: ""

//                when (slot) {
//                    Item.Slot.PRIMARY -> R.color.activePrimary
//                    Item.Slot.SECONDARY -> R.color.activePrimary
//                    Item.Slot.TERTIARY -> R.color.activePrimary
//                    Item.Slot.ARMOR -> R.color.activePrimary
//                    Item.Slot.CLOTHING -> R.color.activePrimary
//                    null -> R.color.disabled
//                }.let {
//                    setTextColor(ResourcesCompat.getColor(resources, it, null))
//                }
//            }
        }

        viewModel.showNotFound.reObserve(this) {
            showSingleChoiceDialog(
                getString(R.string.item_detail_error_title),
                getString(R.string.item_detail_error_message),
                getString(R.string.generic_back),
            ) { findNavController().popBackStack() }
        }
    }
}
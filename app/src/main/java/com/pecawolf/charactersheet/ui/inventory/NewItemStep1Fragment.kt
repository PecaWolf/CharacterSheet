package com.pecawolf.charactersheet.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.databinding.FragmentNewItemStep1Binding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.charactersheet.ui.SimpleSelectionAdapter
import com.pecawolf.model.Item
import com.pecawolf.presentation.SimpleSelectionItem
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.NewItemStep1ViewModel
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class NewItemStep1Fragment : BaseFragment<NewItemStep1ViewModel, FragmentNewItemStep1Binding>() {

    private val itemTypeAdapter: SimpleSelectionAdapter by lazy {
        SimpleSelectionAdapter { viewModel.onItemTypeSelected(it as Item.ItemType) }
            .apply {
                items = Item.ItemType.items().map {
                    SimpleSelectionItem(it.getLocalizedName(requireContext()), false, it)
                }
            }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNewItemStep1Binding.inflate(inflater, container, false)

    override fun createViewModel() = injectVM<NewItemStep1ViewModel>().value

    override fun bindView(binding: FragmentNewItemStep1Binding, viewModel: NewItemStep1ViewModel) {
        binding.newItemStep1NameInput.doAfterTextChanged {
            viewModel.onNameChanged(it.toString())
        }
        binding.newItemStep1DescriptionInput.doAfterTextChanged {
            viewModel.onDescriptionChanged(it.toString())
        }
        binding.newItemStep1TypeRecycler.apply {
            adapter = itemTypeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.newItemStep1ButtonNext.setOnClickListener {
            viewModel.onNextClicked()
        }
    }

    override fun observeViewModel(
        binding: FragmentNewItemStep1Binding,
        viewModel: NewItemStep1ViewModel
    ) {
        viewModel.nameInput.reObserve(this) {
            binding.newItemStep1NameInput.apply {
                if (text.toString() != it) setText(it)
            }
        }
        viewModel.descriptionInput.reObserve(this) {
            binding.newItemStep1DescriptionInput.apply {
                if (text.toString() != it) setText(it)
            }
        }
        viewModel.itemTypes.reObserve(this) { items ->
            itemTypeAdapter.items = items.map {
                SimpleSelectionItem(
                    it.first.getLocalizedName(requireContext()),
                    it.second,
                    it.first
                )
            }
            binding.newItemStep1TypeRecycler.post {
                binding.root.requestLayout()
            }
        }
        viewModel.isNextEnabled.reObserve(this) { isEnabled ->
            binding.newItemStep1ButtonNext.isEnabled = isEnabled
        }
        viewModel.navigateToNext.reObserve(this) {
            findNavController().navigate(
                NewItemStep1FragmentDirections.actionNewItemStep1ToNewItemStep2(
                    it.first,
                    it.second,
                    it.third.name
                )
            )
        }
    }
}
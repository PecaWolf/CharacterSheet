package com.pecawolf.charactersheet.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentMultiChoiceBinding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.presentation.SimpleSelectionItem
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.MultiChoiceViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MultiChoiceFragment : BaseFragment<MultiChoiceViewModel, FragmentMultiChoiceBinding>() {

    private val selectionAdapter: SimpleSelectionAdapter by lazy {
        SimpleSelectionAdapter { viewModel.itemSelected(it!!) }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMultiChoiceBinding.inflate(inflater, container, false)

    override fun createViewModel(): MultiChoiceViewModel = viewModel<MultiChoiceViewModel> {
        MultiChoiceFragmentArgs.fromBundle(requireArguments()).run {
            parametersOf(itemId, field)
        }
    }.value

    override fun bindView(binding: FragmentMultiChoiceBinding, viewModel: MultiChoiceViewModel) {
        binding.multiChoiceRecycler.apply {
            adapter = selectionAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.multiChoiceButtonPositive.setOnClickListener { viewModel.onPositiveButtonClicked() }
        binding.multiChoiceButtonNegative.setOnClickListener { viewModel.onNegativeButtonClicked() }
    }

    override fun observeViewModel(
        binding: FragmentMultiChoiceBinding,
        viewModel: MultiChoiceViewModel
    ) {
        viewModel.isLoading.reObserve(this) { isLoading ->
            binding.multiChoiceProgress.isVisible = isLoading
            binding.multiChoiceButtonPositive.isEnabled = !isLoading
            binding.multiChoiceButtonNegative.isEnabled = !isLoading
        }

        viewModel.field.reObserve(this) {
            binding.multiChoiceHeader.setText(
                when (it) {
                    MultiChoiceViewModel.LOADOUT -> R.string.multi_choice_header_loadout
                    MultiChoiceViewModel.DAMAGE_TYPE -> R.string.multi_choice_header_damage_type
                    MultiChoiceViewModel.DAMAGE -> R.string.multi_choice_header_damage
                    MultiChoiceViewModel.WIELD -> R.string.multi_choice_header_wield
                    else -> throw IllegalStateException("Field type $it not supported!")
                }
            )
            binding.multiChoiceHeader.requestLayout()
        }

        viewModel.loadoutOptions.reObserve(this) { items ->
            selectionAdapter.items = items.map {
                SimpleSelectionItem(
                    getString(it.first.getLocalizedName()),
                    it.second,
                    it.first,
                )
            }
        }

        viewModel.damageTypes.reObserve(this) { items ->
            selectionAdapter.items = items.map {
                SimpleSelectionItem(
                    getString(it.first.getLocalizedName()),
                    it.second,
                    it.first
                )
            }
        }
        viewModel.damageLevels.reObserve(this) { items ->
            selectionAdapter.items = items.map {
                SimpleSelectionItem(
                    getString(it.first.getLocalizedName()),
                    it.second,
                    it.first
                )
            }
        }
        viewModel.wieldOptions.reObserve(this) { items ->
            selectionAdapter.items = items.map {
                SimpleSelectionItem(
                    getString(it.first.getLocalizedName()),
                    it.second,
                    it.first
                )
            }
        }
        viewModel.cancel.reObserve(this) {
            dialog?.cancel()
        }
    }
}
package com.pecawolf.charactersheet.ui.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import com.pecawolf.charactersheet.databinding.FragmentSpeciesSelectionBinding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.charactersheet.ui.SimpleSelectionAdapter
import com.pecawolf.charactersheet.ui.view.initialize
import com.pecawolf.model.BaseStats
import com.pecawolf.presentation.SimpleSelectionItem
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.character.SpeciesSelectionViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SpeciesSelectionFragment :
    BaseFragment<SpeciesSelectionViewModel, FragmentSpeciesSelectionBinding>() {

    private val worldsAdapter: SimpleSelectionAdapter by lazy {
        SimpleSelectionAdapter { viewModel.onWorldSelected(it as BaseStats.World) }
    }

    private val speciesAdapter: SimpleSelectionAdapter by lazy {
        SimpleSelectionAdapter { viewModel.onSpeciesSelected(it as BaseStats.Species) }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSpeciesSelectionBinding.inflate(inflater, container, false)

    override fun createViewModel() = viewModel<SpeciesSelectionViewModel> { parametersOf() }.value

    override fun bindView(
        binding: FragmentSpeciesSelectionBinding,
        viewModel: SpeciesSelectionViewModel
    ) {
        binding.worldsRecycler.initialize(worldsAdapter)
        binding.speciesRecycler.initialize(speciesAdapter)
        binding.continueButton.setOnClickListener { viewModel.onContinueClicked() }
    }

    override fun observeViewModel(
        binding: FragmentSpeciesSelectionBinding,
        viewModel: SpeciesSelectionViewModel
    ) {
        viewModel.worlds.reObserve(this) { list ->
            worldsAdapter.items = list.map {
                SimpleSelectionItem(
                    it.first.getLocalizedName(requireContext()),
                    it.second,
                    it.first
                )
            }
        }
        viewModel.species.reObserve(this) { list ->
            binding.speciesCard.isGone = list.isEmpty()
            speciesAdapter.items = list.map {
                SimpleSelectionItem(
                    it.first.getLocalizedName(requireContext()),
                    it.second,
                    it.first
                )
            }
        }
        viewModel.isContinueEnabled.reObserve(this) { isEnabled ->
            binding.continueButton.isEnabled = isEnabled
        }
        viewModel.navigateToNextStep.reObserve(this) {
            findNavController().navigate(
                SpeciesSelectionFragmentDirections.actionCreateCharacterToBaseStats(
                    it.first.name,
                    it.second.name
                )
            )
        }
    }
}
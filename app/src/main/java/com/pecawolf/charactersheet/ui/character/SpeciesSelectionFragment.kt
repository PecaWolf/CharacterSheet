package com.pecawolf.charactersheet.ui.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentSpeciesSelectionBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.charactersheet.ui.SimpleSelectionAdapter
import com.pecawolf.model.BaseStats
import com.pecawolf.presentation.SimpleSelectionItem
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.character.SpeciesSelectionViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SpeciesSelectionFragment :
    BaseFragment<SpeciesSelectionViewModel, FragmentSpeciesSelectionBinding>() {

    private val worldsAdapter: SimpleSelectionAdapter by lazy {
        SimpleSelectionAdapter(::onWorldSelected)
    }

    private val speciesAdapter: SimpleSelectionAdapter by lazy {
        SimpleSelectionAdapter(::onSpeciesSelected)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSpeciesSelectionBinding.inflate(inflater, container, false)

    override fun createViewModel() = viewModel<SpeciesSelectionViewModel>().value

    override fun bindView(
        binding: FragmentSpeciesSelectionBinding,
        viewModel: SpeciesSelectionViewModel
    ) {
        binding.worldsRecycler.apply {
            adapter = worldsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.speciesRecycler.apply {
            adapter = speciesAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.continueButton.setOnClickListener { viewModel.onContinueClicked() }
    }

    override fun observeViewModel(
        binding: FragmentSpeciesSelectionBinding,
        viewModel: SpeciesSelectionViewModel
    ) {
        viewModel.worlds.reObserve(this) { list ->
            worldsAdapter.items = list.map {
                SimpleSelectionItem(
                    getString(it.first.getLocalizedName()),
                    it.second,
                    it.first
                )
            }
        }
        viewModel.species.reObserve(this) { list ->
            binding.speciesCard.isGone = list.isEmpty()
            speciesAdapter.items = list.map {
                SimpleSelectionItem(
                    getString(it.first.getLocalizedName()),
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

    private fun onWorldSelected(data: Any?) {
        viewModel.onWorldSelected(data as BaseStats.World)
    }

    private fun onSpeciesSelected(data: Any?) {
        viewModel.onSpeciesSelected(data as BaseStats.Species)
    }
}

private fun BaseStats.World.getLocalizedName() = when (this) {
    BaseStats.World.LAST_REALM -> R.string.world_last_realm
    BaseStats.World.BLUE_WAY -> R.string.world_blue_way
    BaseStats.World.DARK_WAY -> R.string.world_dark_way
    BaseStats.World.COLD_FRONTIER -> R.string.world_cold_frontier
}

private fun BaseStats.Species.getLocalizedName() = when (this) {
    BaseStats.Species.HUMAN -> R.string.species_human
    BaseStats.Species.DWARF -> R.string.species_dwarf
    BaseStats.Species.ELF -> R.string.species_elf
    BaseStats.Species.HAVLIN -> R.string.species_havlin
    BaseStats.Species.KARANTI -> R.string.species_karanti
    BaseStats.Species.NATHOREAN -> R.string.species_nathorean
    BaseStats.Species.SEARIAN -> R.string.species_searian
    BaseStats.Species.GUSMERIAN -> R.string.species_gusmerian
    BaseStats.Species.KRUNG -> R.string.species_krung
}

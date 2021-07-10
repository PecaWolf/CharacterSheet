package com.pecawolf.charactersheet.ui.character

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pecawolf.charactersheet.databinding.FragmentBaseStatsBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.model.BaseStats
import com.pecawolf.presentation.viewmodel.character.BaseStatsViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BaseStatsFragment : BaseFragment<BaseStatsViewModel, FragmentBaseStatsBinding>() {

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBaseStatsBinding.inflate(inflater, container, false)

    override fun createViewModel() = viewModel<BaseStatsViewModel> {
        BaseStatsFragmentArgs.fromBundle(requireArguments()).run {
            parametersOf(
                BaseStats.World.valueOf(world),
                BaseStats.Species.valueOf(species)
            )
        }
    }.value

    override fun bindView(binding: FragmentBaseStatsBinding, viewModel: BaseStatsViewModel) {

    }

    override fun observeViewModel(
        binding: FragmentBaseStatsBinding,
        viewModel: BaseStatsViewModel
    ) {

    }
}
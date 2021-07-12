package com.pecawolf.charactersheet.ui.character

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.pecawolf.charactersheet.MainActivity
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentBaseStatsBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.model.BaseStats
import com.pecawolf.presentation.extensions.reObserve
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
        binding.nameInput.addTextChangedListener { viewModel.onNameChanged(it.toString()) }

        binding.basicInfoStrPlus.setOnClickListener { viewModel.onStrengthButtonClicked(true) }
        binding.basicInfoStrMinus.setOnClickListener { viewModel.onStrengthButtonClicked(false) }

        binding.basicInfoDexPlus.setOnClickListener { viewModel.onDexterityButtonClicked(true) }
        binding.basicInfoDexMinus.setOnClickListener { viewModel.onDexterityButtonClicked(false) }

        binding.basicInfoVitPlus.setOnClickListener { viewModel.onVitalityButtonClicked(true) }
        binding.basicInfoVitMinus.setOnClickListener { viewModel.onVitalityButtonClicked(false) }

        binding.basicInfoIntPlus.setOnClickListener { viewModel.onIntelligenceButtonClicked(true) }
        binding.basicInfoIntMinus.setOnClickListener { viewModel.onIntelligenceButtonClicked(false) }

        binding.basicInfoWisPlus.setOnClickListener { viewModel.onWisdomButtonClicked(true) }
        binding.basicInfoWisMinus.setOnClickListener { viewModel.onWisdomButtonClicked(false) }

        binding.basicInfoChaPlus.setOnClickListener { viewModel.onCharismaButtonClicked(true) }
        binding.basicInfoChaMinus.setOnClickListener { viewModel.onCharismaButtonClicked(false) }

        binding.continueButton.setOnClickListener { viewModel.onContinueClicked() }
    }

    override fun observeViewModel(
        binding: FragmentBaseStatsBinding,
        viewModel: BaseStatsViewModel
    ) {
        viewModel.name.reObserve(this) { name ->
            if (binding.nameInput.text.toString() != name) binding.nameInput.setText(name)
        }
        viewModel.strength.reObserve(this) {
            binding.basicInfoStrValue.text = "$it"
        }

        viewModel.dexterity.reObserve(this) {
            binding.basicInfoDexValue.text = "$it"
        }

        viewModel.vitality.reObserve(this) {
            binding.basicInfoVitValue.text = "$it"
        }

        viewModel.intelligence.reObserve(this) {
            binding.basicInfoIntValue.text = "$it"
        }

        viewModel.wisdom.reObserve(this) {
            binding.basicInfoWisValue.text = "$it"
        }

        viewModel.charisma.reObserve(this) {
            binding.basicInfoChaValue.text = "$it"
        }

        viewModel.remaining.reObserve(this) {
            binding.basicInfoRemainingValue.apply {
                text = "$it"
                setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        when {
                            it < 0 -> R.color.buttonBackgroundNegativeEnabled
                            it == 0 -> R.color.buttonBackgroundPositiveEnabled
                            else -> R.color.primary
                        },
                        null
                    )
                )
            }
        }

        viewModel.isContinueEnabled.reObserve(this) { isEnabled ->
            binding.continueButton.isEnabled = isEnabled
        }

        viewModel.navigateToNext.reObserve(this) {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }
}
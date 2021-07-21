package com.pecawolf.charactersheet.ui.home

import android.text.Html
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentHomeBinding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.model.BaseStats
import com.pecawolf.model.RollResult
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.HomeViewModel
import com.pecawolf.presentation.viewmodel.main.HomeViewModel.Destination
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    private val hpAdapter: HpAdapter by lazy { HpAdapter() }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun createViewModel() = injectVM<HomeViewModel>().value

    override fun bindView(binding: FragmentHomeBinding, viewModel: HomeViewModel) {
        binding.homeStrRoll.setOnClickListener { viewModel.onStrRollClicked() }
        binding.homeDexRoll.setOnClickListener { viewModel.onDexRollClicked() }
        binding.homeVitRoll.setOnClickListener { viewModel.onVitRollClicked() }
        binding.homeIntRoll.setOnClickListener { viewModel.onIntRollClicked() }
        binding.homeWisRoll.setOnClickListener { viewModel.onWisRollClicked() }
        binding.homeChaRoll.setOnClickListener { viewModel.onChaRollClicked() }
        binding.homeLuckAndHpRecycler.apply {
            adapter = hpAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            ).apply {
                stackFromEnd = true
            }
        }
        binding.homeHeal.setOnClickListener { viewModel.onHealClicked() }
        binding.homeDamage.setOnClickListener { viewModel.onDamageClicked() }
        binding.homeDamage.setOnLongClickListener { true.also { viewModel.onDamageLongClicked() } }
    }

    override fun observeViewModel(binding: FragmentHomeBinding, viewModel: HomeViewModel) {
        viewModel.isLoading.reObserve(this) { isLoading ->
            binding.homeProgress.isVisible = isLoading
            binding.homeClicker.isVisible = isLoading
            binding.homeClicker.isEnabled = isLoading
        }

        viewModel.baseStats.reObserve(this, { stats ->
            binding.homeNameValue.text = stats.name
            binding.homeSpeciesValue.text = stats.species.getLocalizedName(requireContext())
            binding.homeStrValue.text = stats.str.value.toString()
            binding.homeStrTrap.text = stats.str.trap.toString()
            binding.homeDexValue.text = stats.dex.value.toString()
            binding.homeDexTrap.text = stats.dex.trap.toString()
            binding.homeVitValue.text = stats.vit.value.toString()
            binding.homeVitTrap.text = stats.vit.trap.toString()
            binding.homeIntValue.text = stats.inl.value.toString()
            binding.homeIntTrap.text = stats.inl.trap.toString()
            binding.homeWisValue.text = stats.wis.value.toString()
            binding.homeWisTrap.text = stats.wis.trap.toString()
            binding.homeChaValue.text = stats.cha.value.toString()
            binding.homeChaTrap.text = stats.cha.trap.toString()
        })

        viewModel.luckAndHp.reObserve(this) { luckAndHp ->
            binding.homeLuckAndHpValue.text =
                resources.getString(R.string.home_luck_and_hp, luckAndHp.first, luckAndHp.second)
            hpAdapter.items = luckAndHp
        }

        viewModel.navigateTo.reObserve(this) { destination ->
            when (destination) {
                is Destination.RollModifierDialog -> showRollModifierDialog(destination.stat)
                is Destination.RollResultDialog -> showRollResultDialog(
                    destination.roll,
                    destination.rollResult
                )
            }
        }
    }

    private fun showRollModifierDialog(stat: BaseStats.Stat) {
        dialogHelper.showTextInputDialog(
            getString(R.string.roll_modifier_title, stat.getLocalizedName(requireContext())),
            getString(R.string.roll_modifier_message),
            InputType.TYPE_CLASS_NUMBER,
            1,
            "0",
            getString(R.string.roll_modifier_hint),
            getString(R.string.roll_modifier_positive)
        ) { dialog, modifier ->
            viewModel.onRollConfirmed(stat, modifier)
            dialog.cancel()
        }
    }

    private fun showRollResultDialog(roll: Int, rollResult: RollResult) {
        dialogHelper.showSingleChoiceDialog(
            getString(R.string.roll_result_title),
            Html.fromHtml(
                getString(
                    R.string.roll_result_message,
                    roll,
                    rollResult.getLocalizedName(requireContext())
                )
            )
                .toString(),
            getString(R.string.generic_ok)
        ) { dialog -> dialog.cancel() }
    }
}
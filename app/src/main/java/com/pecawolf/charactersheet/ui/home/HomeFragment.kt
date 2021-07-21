package com.pecawolf.charactersheet.ui.home

import android.text.Html
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.common.extensions.let
import com.pecawolf.charactersheet.databinding.FragmentHomeBinding
import com.pecawolf.charactersheet.databinding.ItemHitPointBinding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.charactersheet.ui.view.DebouncedOnClickListener
import com.pecawolf.model.BaseStats
import com.pecawolf.model.RollResult
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.HomeViewModel
import com.pecawolf.presentation.viewmodel.main.HomeViewModel.Destination
import timber.log.Timber
import androidx.appcompat.widget.LinearLayoutCompat.LayoutParams as LinearLayoutParams
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun createViewModel() = injectVM<HomeViewModel>().value

    override fun bindView(binding: FragmentHomeBinding, viewModel: HomeViewModel) {
        binding.homeStrStat.setOnClickListener { viewModel.onRollClicked(it) }
        binding.homeDexStat.setOnClickListener { viewModel.onRollClicked(it) }
        binding.homeVitStat.setOnClickListener { viewModel.onRollClicked(it) }
        binding.homeInlStat.setOnClickListener { viewModel.onRollClicked(it) }
        binding.homeWisStat.setOnClickListener { viewModel.onRollClicked(it) }
        binding.homeChaStat.setOnClickListener { viewModel.onRollClicked(it) }

        binding.homeHeal.setOnClickListener(DebouncedOnClickListener { clicks ->
            viewModel.onHealClicked(clicks)
        })
        binding.homeDamage.setOnClickListener(DebouncedOnClickListener { clicks ->
            viewModel.onDamageClicked(clicks)
        })
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
            binding.homeStrStat.data = stats.str
            binding.homeDexStat.data = stats.dex
            binding.homeVitStat.data = stats.vit
            binding.homeInlStat.data = stats.inl
            binding.homeWisStat.data = stats.wis
            binding.homeChaStat.data = stats.cha
        })

        viewModel.luckAndHp.reObserve(this) { luckAndHp ->
            luckAndHp.let { luck, wounds ->
                binding.homeLuckAndHpValue.text =
                    resources.getString(R.string.home_luck_and_wounds, luck, wounds)
                binding.homeLuckAndHpRecycler.apply {
                    removeAllViews()
                    for (i in 1..luck) {
                        Timber.d("luck")
                        inflateHitPoint(R.color.luck)
                    }
                    for (i in 1..wounds) {
                        Timber.e("wound")
                        inflateHitPoint(R.color.wound)
                    }
                }
            }
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

    private fun LinearLayoutCompat.inflateHitPoint(color: Int) =
        addView(
            ItemHitPointBinding.inflate(
                layoutInflater,
                this,
                false
            ).apply {
                this.item.setCardBackgroundColor(ResourcesCompat.getColor(resources, color, null))
                this.item.layoutParams = LinearLayoutParams(
                    LinearLayoutParams.MATCH_PARENT,
                    0,
                    1f
                )
            }.root
        )

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
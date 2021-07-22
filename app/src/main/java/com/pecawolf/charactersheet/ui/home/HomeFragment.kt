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
import com.pecawolf.model.RollResult
import com.pecawolf.model.Rollable
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
        binding.homeNameEditIcon.setOnClickListener { viewModel.onNameEdit() }
        binding.homeStrStat.apply {
            setOnRollClickListener { viewModel.onRollClicked(it as Rollable.Stat.Strength) }
            setOnEditClickListener { viewModel.onStatEditClicked(it as Rollable.Stat.Strength) }
        }
        binding.homeDexStat.apply {
            setOnRollClickListener { viewModel.onRollClicked(it as Rollable.Stat.Dexterity) }
            setOnEditClickListener { viewModel.onStatEditClicked(it as Rollable.Stat.Dexterity) }
        }
        binding.homeVitStat.apply {
            setOnRollClickListener { viewModel.onRollClicked(it as Rollable.Stat.Vitality) }
            setOnEditClickListener { viewModel.onStatEditClicked(it as Rollable.Stat.Vitality) }
        }
        binding.homeInlStat.apply {
            setOnRollClickListener { viewModel.onRollClicked(it as Rollable.Stat.Intelligence) }
            setOnEditClickListener { viewModel.onStatEditClicked(it as Rollable.Stat.Intelligence) }
        }
        binding.homeWisStat.apply {
            setOnRollClickListener { viewModel.onRollClicked(it as Rollable.Stat.Wisdom) }
            setOnEditClickListener { viewModel.onStatEditClicked(it as Rollable.Stat.Wisdom) }
        }
        binding.homeChaStat.apply {
            setOnRollClickListener { viewModel.onRollClicked(it as Rollable.Stat.Charisma) }
            setOnEditClickListener { viewModel.onStatEditClicked(it as Rollable.Stat.Charisma) }
        }

        binding.homeHeal.setOnClickListener(DebouncedOnClickListener { clicks ->
            viewModel.onHealClicked(clicks)
        })
        binding.homeDamage.setOnClickListener(DebouncedOnClickListener { clicks ->
            viewModel.onDamageClicked(clicks)
        })
        binding.homeDamage.setOnLongClickListener { true.also { viewModel.onDamageLongClicked() } }
        binding.homeCharacterEditFab.setOnClickListener { viewModel.onEditClicked() }
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

        viewModel.isEditing.reObserve(this) { isEditing ->
            binding.homeNameEditIcon.isVisible = isEditing

            binding.homeStrStat.isEditing = isEditing
            binding.homeDexStat.isEditing = isEditing
            binding.homeVitStat.isEditing = isEditing
            binding.homeInlStat.isEditing = isEditing
            binding.homeWisStat.isEditing = isEditing
            binding.homeChaStat.isEditing = isEditing
        }

        viewModel.navigateTo.reObserve(this) { destination ->
            when (destination) {
                is Destination.EditNameDialog -> showNameDialog(destination.name)
                is Destination.RollModifierDialog -> showRollModifierDialog(destination.stat)
                is Destination.RollResultDialog -> showRollResultDialog(
                    destination.roll,
                    destination.rollResult
                )
                is Destination.StatEditDialog -> showStatEditDialog(destination.stat)
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

    private fun showNameDialog(name: String) {
        dialogHelper.showTextInputDialog(
            title = getString(R.string.character_name_title),
            message = getString(R.string.character_name_message),
            inputType = InputType.TYPE_CLASS_TEXT,
            lineCount = 1,
            defaultInput = name,
            hint = getString(R.string.character_name_hint),
            positiveButton = getString(R.string.generic_ok)
        ) { dialog, name ->
            viewModel.onNameChanged(name)
            dialog.cancel()
        }
    }

    private fun showRollModifierDialog(stat: Rollable.Stat) {
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

    private fun showStatEditDialog(stat: Rollable.Stat) {
        dialogHelper.showTextInputDialog(
            getString(R.string.stat_edit_title, stat.getLocalizedName(requireContext())),
            getString(R.string.stat_edit_message),
            InputType.TYPE_CLASS_NUMBER,
            1,
            stat.value.toString(),
            getString(R.string.stat_edit_hint, stat.getLocalizedName(requireContext()).lowercase()),
            getString(R.string.generic_continue)
        ) { dialog, value ->
            viewModel.onEditConnfirmed(stat.also { it.value = value.toInt() })
            dialog.cancel()
        }
    }
}
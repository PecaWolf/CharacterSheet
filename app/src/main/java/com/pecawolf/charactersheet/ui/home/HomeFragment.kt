package com.pecawolf.charactersheet.ui.home

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentHomeBinding
import com.pecawolf.charactersheet.databinding.ItemHitPointBinding
import com.pecawolf.charactersheet.ext.getLocalizedName
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.charactersheet.ui.view.DebouncedOnClickListener
import com.pecawolf.common.extensions.let2
import com.pecawolf.model.RollResult
import com.pecawolf.model.Rollable
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.HomeViewModel
import com.pecawolf.presentation.viewmodel.main.HomeViewModel.Destination
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import androidx.appcompat.widget.LinearLayoutCompat.LayoutParams as LinearLayoutParams

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun createViewModel() = viewModel<HomeViewModel> { parametersOf() }.value

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
        binding.homeCharacterEditButton.setOnCheckedChangedListener { _, isChecked -> viewModel.onEditClicked(isChecked) }
    }

    override fun observeViewModel(binding: FragmentHomeBinding, viewModel: HomeViewModel) {
        viewModel.isLoading.reObserve(this) { isLoading ->
            binding.homeProgress.isVisible = isLoading
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
            Timber.d("luckAndHp(): $luckAndHp")
            luckAndHp.run {
                binding.homeLuckAndHpValue.text = resources.getString(R.string.home_luck_and_wounds, luckFull, woundsFull)
                binding.homeLuckAndHpRecycler.apply {
                    removeAllViews()
                    for (i in 1..luckEmpty) {
                        Timber.i("luck Empty")
                        inflateHitPoint(R.drawable.bg_hitpoint_luck_empty)
                    }
                    for (i in 1..luckFull) {
                        Timber.d("luck Full")
                        inflateHitPoint(R.drawable.bg_hitpoint_luck_full)
                    }

                    for (i in 1..woundsEmpty) {
                        Timber.e("wound Empty")
                        inflateHitPoint(R.drawable.bg_hitpoint_wound_empty)
                    }
                    for (i in 1..woundsFull) {
                        Timber.w("wound Full")
                        inflateHitPoint(R.drawable.bg_hitpoint_wound_full)
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

            binding.homeCharacterEditButton.isChecked = isEditing
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

    private fun LinearLayoutCompat.inflateHitPoint(background: Int) =
        addView(
            ItemHitPointBinding.inflate(
                layoutInflater,
                this,
                false
            ).apply {
                this.item.background = ResourcesCompat.getDrawable(resources, background, null)
                this.item.layoutParams = LinearLayoutParams(
                    LinearLayoutParams.MATCH_PARENT,
                    0,
                    1f
                ).apply {
                    bottomMargin = resources.getDimensionPixelSize(R.dimen.spacing_1)
                }
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
        dialogHelper.showRollModifierDialog(
            stat
        ) { dialog, modifier ->
            viewModel.onRollConfirmed(stat, modifier)
            dialog.cancel()
        }
    }

    private fun showRollResultDialog(roll: Int, rollResult: RollResult) {
        dialogHelper.showRollResultDialog(roll, rollResult)
    }

    private fun showStatEditDialog(stat: Rollable.Stat) {
        dialogHelper.showRollableEditDialog(stat) { dialog, value ->
            viewModel.onEditConnfirmed(stat.also { it.value = value.toInt() })
            dialog.cancel()
        }
    }
}
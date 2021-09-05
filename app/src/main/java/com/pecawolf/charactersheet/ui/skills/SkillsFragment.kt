package com.pecawolf.charactersheet.ui.skills

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.pecawolf.charactersheet.databinding.FragmentSkillsBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.charactersheet.ui.view.initialize
import com.pecawolf.model.RollResult
import com.pecawolf.model.Rollable
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.SkillsViewModel
import com.pecawolf.presentation.viewmodel.main.SkillsViewModel.Destination
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class SkillsFragment : BaseFragment<SkillsViewModel, FragmentSkillsBinding>() {

    private val skillsAdapter: SkillsAdapter by lazy {
        SkillsAdapter(
            { skill -> viewModel.onRollClicked(skill as Rollable.Skill) },
            { skill -> viewModel.onEditClicked(skill as Rollable.Skill) },
        )
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) = FragmentSkillsBinding.inflate(inflater, container, false)

    override fun createViewModel() = viewModel<SkillsViewModel> { parametersOf() }.value

    override fun bindView(binding: FragmentSkillsBinding, viewModel: SkillsViewModel) {
        binding.skillsRecycler.initialize(skillsAdapter)
        binding.skillsFiltersButton.setOnCheckedChangedListener { _, isChecked -> viewModel.onSkillsFiltersClicked(isChecked) }
        binding.skillsEditButton.setOnCheckedChangedListener { _, isChecked -> viewModel.onSkillsEditClicked(isChecked) }
        binding.skillsUnknownButton.setOnCheckedChangedListener { _, isChecked -> viewModel.onSkillsShowUnknownClicked(isChecked) }
        binding.skillsSearch.setOnSearchChangedListener { viewModel.onSearchChanged(it) }
    }

    override fun observeViewModel(binding: FragmentSkillsBinding, viewModel: SkillsViewModel) {
        viewModel.isLoading.reObserve(this) { isLoading ->
            binding.skillsProgress.isVisible = isLoading
        }
        viewModel.items.reObserve(this) {
            Timber.v("onItems(): ${it.map { it.size }}")
            skillsAdapter.items = it
        }
        viewModel.isShowingFilters.reObserve(this) { isShowing ->
            binding.skillsFiltersButton.isChecked = isShowing
            binding.skillsFilterCard.isVisible = isShowing
        }
        viewModel.isEditing.reObserve(this) { isEditing ->
            skillsAdapter.isEditing = isEditing
        }
        viewModel.isShowingUnknown.reObserve(this) { isShowingUnknown ->
            binding.skillsUnknownButton.isChecked = isShowingUnknown
        }
        viewModel.search.reObserve(this) { search ->
            binding.skillsSearch.text = search
        }
        viewModel.navigateTo.reObserve(this) {
            when (it) {
                is Destination.RollModifierDialog -> showRollModifierDialog(it.skill)
                is Destination.RollResultDialog -> showRollResultDialog(it.rollResult)
                is Destination.SkillEditDialog -> showSkillEditDialog(it.skill)
            }
        }
    }

    private fun showRollModifierDialog(skill: Rollable.Skill) {
        dialogHelper.showRollModifierDialog(
            skill
        ) { dialog, modifier ->
            viewModel.onRollConfirmed(skill, modifier)
            dialog.cancel()
        }
    }

    private fun showRollResultDialog(rollResult: RollResult) {
        dialogHelper.showRollResultDialog(rollResult)
    }

    private fun showSkillEditDialog(skill: Rollable.Skill) {
        dialogHelper.showRollableEditDialog(skill) { dialog, value ->
            viewModel.onEditConnfirmed(skill.also { it.value = value.toInt() })
            dialog.cancel()
        }
    }
}
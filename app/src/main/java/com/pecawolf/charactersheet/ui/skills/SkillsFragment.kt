package com.pecawolf.charactersheet.ui.skills

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentSkillsBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.charactersheet.ui.view.DebouncedTextChangeListener
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
        binding.skillsRecycler.apply {
            adapter = skillsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.skillsEditFab.setOnClickListener { viewModel.onSkillsEditClicked() }
        binding.skillsShowUnknownFab.setOnClickListener { viewModel.onSkillsShowUnknownClicked() }
        binding.skillSearchInput.apply {
            addTextChangedListener(DebouncedTextChangeListener { viewModel.onSearchChanged(it) })
            addTextChangedListener { binding.skillSearchCancel.isVisible = !it.isNullOrBlank() }
            setOnFocusChangeListener { _, hasFocus -> binding.skillSearchIcon.isChecked = hasFocus }
        }
        binding.skillSearchCancel.setOnClickListener { viewModel.onSearchCancelClicked() }
    }

    override fun observeViewModel(binding: FragmentSkillsBinding, viewModel: SkillsViewModel) {
        viewModel.isLoading.reObserve(this) { isLoading ->
            binding.skillsProgress.isVisible = isLoading
        }
        viewModel.items.reObserve(this) {
            Timber.v("onItems(): ${it.map { it.size }}")
            skillsAdapter.items = it
        }
        viewModel.isEditing.reObserve(this) { isEditing ->
            skillsAdapter.isEditing = isEditing
        }
        viewModel.isShowingUnknown.reObserve(this) { isShowingUnknown ->
            binding.skillsShowUnknownFab.labelText =
                getString(if (isShowingUnknown) R.string.skills_unknown_hide else R.string.skills_unknown_show)
        }
        viewModel.search.reObserve(this) { search ->
            binding.skillSearchInput.apply {
                if (text.toString() != search) setText(search)
            }
        }
        viewModel.navigateTo.reObserve(this) {
            when (it) {
                is Destination.RollModifierDialog -> showRollModifierDialog(it.skill)
                is Destination.RollResultDialog -> showRollResultDialog(it.roll, it.rollResult)
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

    private fun showRollResultDialog(roll: Int, rollResult: RollResult) {
        dialogHelper.showRollResultDialog(roll, rollResult)
    }

    private fun showSkillEditDialog(skill: Rollable.Skill) {
        dialogHelper.showRollableEditDialog(skill) { dialog, value ->
            viewModel.onEditConnfirmed(skill.also { it.value = value.toInt() })
            dialog.cancel()
        }
    }
}
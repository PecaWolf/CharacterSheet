package com.pecawolf.charactersheet.ui.skills

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.databinding.FragmentSkillsBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.presentation.extensions.reObserve
import com.pecawolf.presentation.viewmodel.main.SkillsViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SkillsFragment : BaseFragment<SkillsViewModel, FragmentSkillsBinding>() {

    private val skillsAdapter: SkillsAdapter by lazy {
        SkillsAdapter()
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
    }

    override fun observeViewModel(binding: FragmentSkillsBinding, viewModel: SkillsViewModel) {
        viewModel.items.reObserve(this) {
            skillsAdapter.items = it
        }
    }
}
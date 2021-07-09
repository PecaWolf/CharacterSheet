package com.pecawolf.charactersheet.ui.skills

import androidx.fragment.app.Fragment
import com.pecawolf.presentation.viewmodel.SkillsViewModel
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class SkillsFragment : Fragment() {

    private val viewModel: SkillsViewModel by injectVM()

}
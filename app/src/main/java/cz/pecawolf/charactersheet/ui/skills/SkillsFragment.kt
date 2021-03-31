package cz.pecawolf.charactersheet.ui.skills

import androidx.fragment.app.Fragment
import cz.pecawolf.charactersheet.presentation.SkillsViewModel
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class SkillsFragment : Fragment() {

    private val viewModel: SkillsViewModel by injectVM()

}
package com.pecawolf.charactersheet.ui.other

import androidx.fragment.app.Fragment
import com.pecawolf.presentation.OtherViewModel
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class OtherFragment : Fragment() {

    private val viewModel: OtherViewModel by injectVM()
}
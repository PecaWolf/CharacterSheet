package cz.pecawolf.charactersheet.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cz.pecawolf.charactersheet.databinding.FragmentHomeBinding
import cz.pecawolf.charactersheet.presentation.HomeViewModel
import cz.pecawolf.charactersheet.ui.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by injectVM()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun bindView(binding: FragmentHomeBinding) {
        binding.vm = viewModel
    }
}
package cz.pecawolf.charactersheet.ui.home

import androidx.recyclerview.widget.LinearLayoutManager
import cz.pecawolf.charactersheet.R
import cz.pecawolf.charactersheet.databinding.FragmentHomeBinding
import cz.pecawolf.charactersheet.presentation.HomeViewModel
import cz.pecawolf.charactersheet.presentation.extensions.reObserve
import cz.pecawolf.charactersheet.ui.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    private val hpAdapter: HpAdapter by lazy {
        HpAdapter().also {
            binding.luckAndHp.apply {
                adapter = it
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).apply {
                        stackFromEnd = true
                    }
            }
        }
    }

    override fun getLayoutResource() = R.layout.fragment_home
    override fun createViewModel() = injectVM<HomeViewModel>().value

    override fun bindView(binding: FragmentHomeBinding) {
        binding.vm = viewModel

        binding.heal.setOnClickListener { viewModel.onHealClicked() }
        binding.damage.setOnClickListener { viewModel.onDamageClicked() }
        binding.damage.setOnLongClickListener { true.also { viewModel.onDamageLongClicked() } }
    }

    override fun observeViewModel() {
        viewModel.baseStats.reObserve(this, { stats ->
            hpAdapter.items = stats.run { luck to wounds }
        })
    }
}
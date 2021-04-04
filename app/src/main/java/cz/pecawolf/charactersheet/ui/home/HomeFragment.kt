package cz.pecawolf.charactersheet.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cz.pecawolf.charactersheet.databinding.FragmentHomeBinding
import cz.pecawolf.charactersheet.presentation.HomeViewModel
import cz.pecawolf.charactersheet.presentation.extensions.reObserve
import cz.pecawolf.charactersheet.ui.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

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
    private val viewModel: HomeViewModel by injectVM()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun bindView(binding: FragmentHomeBinding) {
        binding.vm = viewModel

        binding.heal.setOnClickListener { viewModel.onHealClicked() }
        binding.damage.setOnClickListener { viewModel.onDamageClicked() }
        binding.damage.setOnLongClickListener { true.also { viewModel.onDamageLongClicked() } }
    }

    override fun observeViewModel() {
        viewModel.baseStats.reObserve(this, { stats ->
            Log.d("HECK", "baseStats.reObserve(): ${stats.luck} + ${stats.wounds}")
            hpAdapter.items = stats.run { luck to wounds }
        })
    }
}
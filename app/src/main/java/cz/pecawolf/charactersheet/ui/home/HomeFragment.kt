package cz.pecawolf.charactersheet.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import cz.pecawolf.charactersheet.R
import cz.pecawolf.charactersheet.common.let2
import cz.pecawolf.charactersheet.databinding.FragmentHomeBinding
import cz.pecawolf.charactersheet.presentation.HomeViewModel
import cz.pecawolf.charactersheet.presentation.extensions.reObserve
import cz.pecawolf.charactersheet.ui.BaseFragment
import timber.log.Timber
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

        binding.strRoll.setOnClickListener { viewModel.onStrRollClicked() }
        binding.dexRoll.setOnClickListener { viewModel.onDexRollClicked() }
        binding.vitRoll.setOnClickListener { viewModel.onVitRollClicked() }
        binding.inlRoll.setOnClickListener { viewModel.onInlRollClicked() }
        binding.wisRoll.setOnClickListener { viewModel.onWisRollClicked() }
        binding.chaRoll.setOnClickListener { viewModel.onChaRollClicked() }
    }

    override fun observeViewModel() {
        viewModel.baseStats.reObserve(this) { stats ->
            Timber.d("baseStats.reObserve(): ${stats.luck} + ${stats.wounds}")
            hpAdapter.items = stats.run { luck to wounds }
        }

        viewModel.rollResult.reObserve(this) { result ->
            result.let2 { roll, success ->
                Snackbar.make(
                    binding.root,
                    getSuccessString(roll, success),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getSuccessString(roll: Int, success: Boolean): String = resources.getString(
        when {
            roll == 1 -> R.string.roll_success_critical
            roll == 20 -> R.string.roll_failure_critical
            success -> R.string.roll_success
            else -> R.string.roll_failure
        },
        roll
    )
}
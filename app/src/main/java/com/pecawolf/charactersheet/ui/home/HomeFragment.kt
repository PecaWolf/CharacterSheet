package com.pecawolf.charactersheet.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.pecawolf.charactersheet.R
import com.pecawolf.charactersheet.databinding.FragmentHomeBinding
import com.pecawolf.charactersheet.ui.BaseFragment
import com.pecawolf.presentation.HomeViewModel
import com.pecawolf.presentation.extensions.reObserve
import org.koin.android.viewmodel.ext.android.viewModel as injectVM

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    private val hpAdapter: HpAdapter by lazy {
        HpAdapter().also {
            binding.luckAndHpRecycler.apply {
                adapter = it
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL, false
                ).apply {
                    stackFromEnd = true
                }
//                addItemDecoration(
//                    SpacingDecoration(resources.getDimension(R.dimen.spacing_1).toInt())
//                )
            }
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun createViewModel() = injectVM<HomeViewModel>().value

    override fun bindView(binding: FragmentHomeBinding, viewModel: HomeViewModel) {
        binding.heal.setOnClickListener { viewModel.onHealClicked() }
        binding.damage.setOnClickListener { viewModel.onDamageClicked() }
        binding.damage.setOnLongClickListener { true.also { viewModel.onDamageLongClicked() } }
    }

    override fun observeViewModel(binding: FragmentHomeBinding, viewModel: HomeViewModel) {
        viewModel.baseStats.reObserve(this, { stats ->
            binding.nameValue.text = stats.name
            binding.speciesValue.text = stats.species.standardName
            binding.strValue.text = stats.strength
            binding.strTrap.text = stats.strengthTrap
            binding.dexValue.text = stats.dexterity
            binding.dexTrap.text = stats.dexterityTrap
            binding.vitValue.text = stats.vitality
            binding.vitTrap.text = stats.vitalityTrap
            binding.intValue.text = stats.inteligence
            binding.intTrap.text = stats.inteligenceTrap
            binding.wisValue.text = stats.wisdom
            binding.wisTrap.text = stats.wisdomTrap
            binding.chaValue.text = stats.charisma
            binding.chaTrap.text = stats.charismaTrap
        })

        viewModel.luckAndHp.reObserve(this) { luckAndHp ->
            binding.luckAndHpValue.text =
                resources.getString(R.string.home_luck_and_hp, luckAndHp.first, luckAndHp.second)
            hpAdapter.items = luckAndHp
        }
    }
}
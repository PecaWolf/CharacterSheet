package com.pecawolf.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.pecawolf.model.Rollable.Skill
import com.pecawolf.presentation.viewmodel.BaseViewModel

class SkillsViewModel(val mainViewModel: MainViewModel) : BaseViewModel() {

    private val _items = mainViewModel.character.map {
        listOf(
            setOf(
                Skill("BLACKSMITH", "Blacksmithing", it.str, 1),
                Skill("MINING", "Mining", it.str, 1),
            ),
            setOf(
                Skill("FENCING", "Fencing", it.dex, 1),
                Skill("ARCHERY", "Archery", it.dex, 1),
            ),
            setOf(
                Skill("ASKETISM", "Asketism", it.vit, 1),
                Skill("HARD_LABOR", "Hard labor", it.vit, 1),
            ),
            setOf(
                Skill("RUNESMITH", "Runesmithing", it.wis, 1),
                Skill("Alchemy", "Alchemy", it.wis, 1),
            ),
            setOf(
                Skill("PYROMANCY", "Pyromancy", it.inl, 1),
                Skill("Caelomancy", "Caelomancy", it.inl, 1),
            ),
            setOf(
                Skill("PERSUASION", "Persuasion", it.cha, 1),
                Skill("HAGGLING", "Haggling", it.cha, 1),
            ),
        )
    }
    val items: LiveData<List<Set<Skill>>> = _items
}
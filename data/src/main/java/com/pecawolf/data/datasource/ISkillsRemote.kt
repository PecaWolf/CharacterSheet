package com.pecawolf.data.datasource

import com.pecawolf.data.model.SkillsData
import io.reactivex.rxjava3.core.Observable

interface ISkillsRemote {
    fun observeSkills(): Observable<List<SkillsData>>
}

package com.pecawolf.remote.skills

import com.pecawolf.remote.model.StatSkillsResponse
import io.reactivex.rxjava3.core.Observable

interface ISkillsRemote {
    fun observeSkills(): Observable<List<StatSkillsResponse>>
}

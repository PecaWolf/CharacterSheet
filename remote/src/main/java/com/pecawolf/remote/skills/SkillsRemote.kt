package com.pecawolf.remote.skills

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pecawolf.data.datasource.ISkillsRemote
import com.pecawolf.data.model.SkillsData
import com.pecawolf.remote.mapper.SkillsResponseMapper
import com.pecawolf.remote.model.SkillsResponse
import io.reactivex.rxjava3.core.Observable

class SkillsRemote(
    private val database: FirebaseDatabase,
    private val skillsMapper: SkillsResponseMapper,
) : ISkillsRemote {

    override fun observeSkills(): Observable<List<SkillsData>> = getSkills()
        .distinctUntilChanged()
        .map {
            it.map { skillsMapper.fromResponse(it) }
        }

    private fun getSkills(): Observable<List<SkillsResponse>> =
        Observable.create { emitter ->
            database.reference.child("availableSkills")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children
                            .mapNotNull { it.getValue(SkillsResponse::class.java) }
                            .sortedBy { it.stat }
                            .let {
                                emitter.onNext(it)
                            }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        emitter.onComplete()
                    }
                })
        }
}

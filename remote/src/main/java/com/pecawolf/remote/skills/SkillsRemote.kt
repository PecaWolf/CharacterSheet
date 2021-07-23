package com.pecawolf.remote.skills

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pecawolf.remote.model.StatSkillsResponse
import io.reactivex.rxjava3.core.Observable

class SkillsRemote(
    private val database: FirebaseDatabase,
) : ISkillsRemote {

    override fun observeSkills(): Observable<List<StatSkillsResponse>> = getSkills()
        .distinctUntilChanged()

    private fun getSkills(): Observable<List<StatSkillsResponse>> = Observable.create { emitter ->
        database.reference.child("availableSkills")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children
                        .mapNotNull { it.getValue(StatSkillsResponse::class.java) }
                        .sortedBy { it.stat }
                        .let { emitter.onNext(it) }
                }

                override fun onCancelled(error: DatabaseError) {
                    emitter.onComplete()
                }
            })
    }
}

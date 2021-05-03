package cz.pecawolf.charactersheet

import android.content.res.Resources
import com.google.firebase.firestore.FirebaseFirestore
import cz.pecawolf.charactersheet.common.IRemote
import cz.pecawolf.charactersheet.common.model.BaseStats
import io.reactivex.rxjava3.core.Single
import timber.log.Timber

class RemoteImpl(val firestore: FirebaseFirestore) : IRemote {
    override fun getCharacter(id: String): Single<BaseStats> {
        return Single.create { emitter ->
            firestore.collection("characters")
                .addSnapshotListener { snapshot, error ->
                    snapshot
                        ?.documents
                        ?.firstOrNull { it.id == id }
                        ?.let { doc ->
                            BaseStats(
                                doc.getString("name")
                                    ?: throw Resources.NotFoundException("name not found"),
                                doc.getString("species")
                                    ?.let { BaseStats.Species.fromName(it) }
                                    ?: throw Resources.NotFoundException("species not found"),
                                doc.getLong("luck")?.toInt()
                                    ?: throw Resources.NotFoundException("luck not found"),
                                doc.getLong("wounds")?.toInt()
                                    ?: throw Resources.NotFoundException("wounds not found"),
                                doc.getLong("str")?.toInt()
                                    ?.let { BaseStats.CharacterStat(it) }
                                    ?: throw Resources.NotFoundException("str not found"),
                                doc.getLong("dex")?.toInt()
                                    ?.let { BaseStats.CharacterStat(it) }
                                    ?: throw Resources.NotFoundException("dex not found"),
                                doc.getLong("vit")?.toInt()
                                    ?.let { BaseStats.CharacterStat(it) }
                                    ?: throw Resources.NotFoundException("vit not found"),
                                doc.getLong("inl")?.toInt()
                                    ?.let { BaseStats.CharacterStat(it) }
                                    ?: throw Resources.NotFoundException("inl not found"),
                                doc.getLong("wis")?.toInt()
                                    ?.let { BaseStats.CharacterStat(it) }
                                    ?: throw Resources.NotFoundException("wis not found"),
                                doc.getLong("cha")?.toInt()
                                    ?.let { BaseStats.CharacterStat(it) }
                                    ?: throw Resources.NotFoundException("cha not found"),
                                doc.getLong("money")?.toInt()
                                    ?: throw Resources.NotFoundException("money not found")
                            )
                        }
                        ?.let {
                            emitter.onSuccess(it)
                        } ?: emitter.onError(error)
                }
        }
    }

    override fun setCharacter(
        id: String?,
        baseStats: BaseStats
    ) {
        val stats = baseStats.run {
            hashMapOf(
                "name" to name,
                "species" to species.standardName,
                "luck" to luck,
                "wounds" to wounds,
                "str" to str,
                "dex" to dex,
                "vit" to vit,
                "inl" to inl,
                "wis" to wis,
                "cha" to cha,
                "money" to money
            )
        }

        if (id != null) {
            firestore.collection("characters")
                .document(id)
                .set(stats)
        } else {
            firestore.collection("characters").add(stats)
        }
            .addOnSuccessListener {
                Timber.d("onSuccess()")
            }
            .addOnFailureListener {
                Timber.w(it, "onFailure()")
            }
    }
}

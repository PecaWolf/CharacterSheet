package cz.pecawolf.charactersheet

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import cz.pecawolf.charactersheet.common.IRemote
import cz.pecawolf.charactersheet.common.model.BaseStatsEntity

class RemoteImpl(val firestore: FirebaseFirestore) : IRemote {
    override fun foo(): String {
        return firestore.app.name
    }

    override fun setCharacter(baseStats: BaseStatsEntity) {
        val stats = baseStats.run {
            hashMapOf(
                "name" to name,
                "species" to species,
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

        firestore.collection("characters")
            .add(stats)
            .addOnCompleteListener { Log.d("HECKERY", "onComplete()") }
            .addOnCanceledListener { Log.w("HECKERY", "onCanceled()") }
            .addOnSuccessListener {
                Log.d("HECKERY", "onSuccess(): ${it.id}")
            }
            .addOnFailureListener {
                Log.w("HECKERY", "onFailure()", it)
            }
    }
}

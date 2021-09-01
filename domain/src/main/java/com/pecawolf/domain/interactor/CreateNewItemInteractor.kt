package com.pecawolf.domain.interactor

import com.pecawolf.domain.repository.ICharacterRepository
import com.pecawolf.model.Item
import io.reactivex.rxjava3.core.Single

class CreateNewItemInteractor(private val repository: ICharacterRepository) :
    SingleInteractor<CreateNewItemInteractor.Params, Long>() {

    override fun execute(params: Params): Single<Long> = params.run {
        repository.createItemForActiveCharacter(
            name,
            description,
            type,
            loadouts,
            damage,
            wield,
            magazineSize,
            rateOfFire,
            damageTypes
        )
    }

    data class Params(
        val name: String,
        val description: String,
        val type: Item.ItemType,
        val loadouts: List<Item.LoadoutType>,
        val damage: Item.Damage,
        val wield: Item.Weapon.Wield?,
        val magazineSize: Int,
        val rateOfFire: Int,
        val damageTypes: MutableSet<Item.DamageType>
    )
}

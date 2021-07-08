package com.pecawolf.cache

import com.pecawolf.cache.model.BaseStatsEntity
import com.pecawolf.cache.model.CharacterEntity
import com.pecawolf.cache.model.EquipmentEntity
import io.reactivex.rxjava3.core.Single

class Cache {
    fun getActiveCharacter() = Single.just(
        CharacterEntity(
            BaseStatsEntity(
                "Mathias Caldera",
                "HUMAN",
                10,
                4,
                5,
                6,
                4,
                7,
                6,
                8,
                1000
            ),
            EquipmentEntity(
                EquipmentEntity.Item.Weapon.Ranged.Rifle(
                    "AR-15",
                    "Awesome assault rifle"
                ),
                EquipmentEntity.Item.Weapon.Ranged.Pistol(
                    "Glock",
                    "Awesome pistol"
                ),
                EquipmentEntity.Item.Weapon.Melee.Sword(
                    "Falcata",
                    "Awesome tactical sword",
                    allowedLoadouts = listOf(
                        EquipmentEntity.Item.LoadoutType.COMBAT,
                        EquipmentEntity.Item.LoadoutType.TRAVEL
                    )
                ),
                EquipmentEntity.Item.Armor.Clothing(
                    "Kevlar-reinforced suit",
                    "Style AND protection",
                ),
                EquipmentEntity.Item.Armor.VacArmor(
                    "Light vac-armor",
                    "Kick ass anywhere"
                )
            )
        )
    )
}
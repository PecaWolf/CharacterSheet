package com.pecawolf.cache.mapper

import com.pecawolf.cache.model.ItemEntity
import com.pecawolf.data.model.ItemData

class ItemEntityMapper {

    fun fromEntity(entity: ItemEntity): ItemData = entity.run {
        ItemData(
            itemId,
            ownerId,
            type,
            name,
            description,
            count,
            allowedLoadouts,
            enhancements,
            damage,
            wield,
            damageTypes,
            magazine,
            rateOfFire,
            magazineCount,
            magazineState,
        )
    }

    fun toEntity(model: ItemData) = model.run {
        ItemEntity(
            itemId,
            ownerId,
            type,
            name,
            description,
            count,
            allowedLoadouts,
            enhancements,
            damage,
            wield,
            damageTypes,
            magazineSize,
            rateOfFire,
            magazineCount,
            magazineState,
        )
    }
}
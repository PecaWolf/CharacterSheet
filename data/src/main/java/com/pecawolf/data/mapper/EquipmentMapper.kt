package com.pecawolf.data.mapper

import com.pecawolf.cache.model.EquipmentEntity
import com.pecawolf.remote.model.EquipmentResponse

class EquipmentMapper :
    BaseMapper<com.pecawolf.model.Equipment, EquipmentResponse, EquipmentEntity> {
    override fun fromResponse(response: EquipmentResponse) = response.run {
        com.pecawolf.model.Equipment(
            primary = com.pecawolf.model.Equipment.Item.Weapon.Ranged.Rifle("", ""),
            secondary = com.pecawolf.model.Equipment.Item.Weapon.Ranged.Pistol("", ""),
            tertiary = com.pecawolf.model.Equipment.Item.Weapon.Melee.Knife("", ""),
            clothes = com.pecawolf.model.Equipment.Item.Armor.Clothing("", ""),
            armor = com.pecawolf.model.Equipment.Item.Armor.Clothing("", "")
        )
    }

    override fun fromEntity(entity: EquipmentEntity) = entity.run {
        com.pecawolf.model.Equipment(
            primary = com.pecawolf.model.Equipment.Item.Weapon.Ranged.Rifle("", ""),
            secondary = com.pecawolf.model.Equipment.Item.Weapon.Ranged.Pistol("", ""),
            tertiary = com.pecawolf.model.Equipment.Item.Weapon.Melee.Knife("", ""),
            clothes = com.pecawolf.model.Equipment.Item.Armor.Clothing("", ""),
            armor = com.pecawolf.model.Equipment.Item.Armor.Clothing("", "")
        )
    }

    override fun toEntity(model: com.pecawolf.model.Equipment) = model.run {
        EquipmentEntity(
            primary = EquipmentEntity.Item.Weapon.Ranged.Rifle("", ""),
            secondary = EquipmentEntity.Item.Weapon.Ranged.Pistol("", ""),
            tertiary = EquipmentEntity.Item.Weapon.Melee.Knife("", ""),
            clothes = EquipmentEntity.Item.Armor.Clothing("", ""),
            armor = EquipmentEntity.Item.Armor.Clothing("", "")
        )
    }
}
package com.pecawolf.cache.model

import com.pecawolf.charactersheet.common.isOneOf

data class EquipmentEntity(
    val primary: Item.Weapon,
    val secondary: Item.Weapon,
    val tertiary: Item.Weapon,
    val clothes: Item.Armor,
    val armor: Item.Armor
) {

    sealed class Item(
        open val name: String,
        open val description: String,
        open val allowedLoadouts: List<LoadoutType>,
        open val enhancements: List<Enhancement> = listOf()
    ) {

        sealed class Weapon(
            name: String,
            description: String,
            enhancements: List<Enhancement>,
            allowedLoadouts: List<LoadoutType>,
            open val damage: Damage,
            open val wield: Wield,
            open val damageTypes: Set<DamageType>,
        ) : Item(name, description, allowedLoadouts, enhancements) {

            sealed class Melee(
                name: String,
                description: String,
                enhancements: List<Enhancement>,
                allowedLoadouts: List<LoadoutType>,
                damage: Damage,
                wield: Wield,
                damageTypes: Set<DamageType>,
            ) : Weapon(
                name,
                description,
                enhancements,
                allowedLoadouts,
                damage,
                wield,
                damageTypes
            ) {

                data class Knife(
                    override val name: String,
                    override val description: String,
                    override val enhancements: List<Enhancement> = listOf(),
                    override val allowedLoadouts: List<LoadoutType> = LoadoutType.values().toList(),
                    override val damage: Damage = Damage.LIGHT,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.PIERCE,
                        DamageType.SLASH
                    ),
                ) : Melee(
                    name,
                    description,
                    enhancements,
                    allowedLoadouts,
                    damage,
                    Wield.ONE_HANDED,
                    damageTypes
                )

                data class Sword(
                    override val name: String,
                    override val description: String,
                    override val enhancements: List<Enhancement> = listOf(),
                    override val allowedLoadouts: List<LoadoutType> = listOf(LoadoutType.COMBAT),
                    override val damage: Damage = Damage.MEDIUM,
                    override val wield: Wield = Wield.ONE_HANDED,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.PIERCE,
                        DamageType.SLASH
                    ),
                ) : Melee(
                    name,
                    description,
                    enhancements,
                    allowedLoadouts,
                    damage,
                    wield,
                    damageTypes
                )

                data class Axe(
                    override val name: String,
                    override val description: String,
                    override val enhancements: List<Enhancement> = listOf(),
                    override val allowedLoadouts: List<LoadoutType> = listOf(LoadoutType.COMBAT),
                    override val damage: Damage = Damage.MEDIUM,
                    override val wield: Wield = Wield.ONE_HANDED,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.SLASH,
                        DamageType.BLUNT
                    ),
                ) : Melee(
                    name,
                    description,
                    enhancements,
                    allowedLoadouts,
                    damage,
                    wield,
                    damageTypes
                )
            }

            sealed class Ranged(
                name: String,
                description: String,
                enhancements: List<Enhancement>,
                allowedLoadouts: List<LoadoutType>,
                damage: Damage,
                wield: Wield,
                damageTypes: Set<DamageType>,
                open val magazine: Int,
                open val rateOfFire: Int
            ) : Weapon(
                name,
                description,
                enhancements,
                allowedLoadouts,
                damage,
                wield,
                damageTypes
            ) {

                // region Primitive Weapons

                data class Bow(
                    override val name: String,
                    override val description: String,
                    override val enhancements: List<Enhancement> = listOf(),
                    override val allowedLoadouts: List<LoadoutType> = LoadoutType.values().toList(),
                    override val damage: Damage = Damage.LIGHT,
                    override val damageTypes: Set<DamageType> = setOf(DamageType.PIERCE),
                    override val magazine: Int = 1,
                    override val rateOfFire: Int = 1
                ) : Ranged(
                    name,
                    description,
                    enhancements,
                    allowedLoadouts,
                    damage,
                    Wield.TWO_HANDED,
                    damageTypes,
                    magazine,
                    rateOfFire
                )

                data class CrossBow(
                    override val name: String,
                    override val description: String,
                    override val enhancements: List<Enhancement> = listOf(),
                    override val allowedLoadouts: List<LoadoutType> = LoadoutType.values().toList(),
                    override val damage: Damage = Damage.MEDIUM,
                    override val damageTypes: Set<DamageType> = setOf(DamageType.PIERCE),
                    override val magazine: Int = 1,
                    override val rateOfFire: Int = 1
                ) : Ranged(
                    name,
                    description,
                    enhancements,
                    allowedLoadouts,
                    damage,
                    Wield.TWO_HANDED,
                    damageTypes,
                    magazine,
                    rateOfFire
                )

                // endregion Primitive Weapons

                // region Firearms

                data class Pistol(
                    override val name: String,
                    override val description: String,
                    override val enhancements: List<Enhancement> = listOf(),
                    override val allowedLoadouts: List<LoadoutType> = LoadoutType.values().toList(),
                    override val damage: Damage = Damage.LIGHT,
                    override val damageTypes: Set<DamageType> = setOf(DamageType.BALLISTIC),
                    override val magazine: Int = 12,
                    override val rateOfFire: Int = 2
                ) : Ranged(
                    name,
                    description,
                    enhancements,
                    allowedLoadouts,
                    damage,
                    Wield.ONE_HANDED,
                    damageTypes,
                    magazine,
                    rateOfFire
                )

                data class Revolver(
                    override val name: String,
                    override val description: String,
                    override val enhancements: List<Enhancement> = listOf(),
                    override val allowedLoadouts: List<LoadoutType> = LoadoutType.values().toList(),
                    override val damage: Damage = Damage.LIGHT,
                    override val damageTypes: Set<DamageType> = setOf(DamageType.BALLISTIC),
                    override val magazine: Int = 6,
                    override val rateOfFire: Int = 1
                ) : Ranged(
                    name,
                    description,
                    enhancements,
                    allowedLoadouts,
                    damage,
                    Wield.ONE_HANDED,
                    damageTypes,
                    magazine,
                    rateOfFire
                )

                data class Rifle(
                    override val name: String,
                    override val description: String,
                    override val enhancements: List<Enhancement> = listOf(),
                    override val allowedLoadouts: List<LoadoutType> = listOf(LoadoutType.COMBAT),
                    override val damage: Damage = Damage.MEDIUM,
                    override val damageTypes: Set<DamageType> = setOf(DamageType.BALLISTIC),
                    override val magazine: Int = 20,
                    override val rateOfFire: Int = 5
                ) : Ranged(
                    name,
                    description,
                    enhancements,
                    allowedLoadouts,
                    damage,
                    Wield.TWO_HANDED,
                    damageTypes,
                    magazine,
                    rateOfFire
                )

                data class SubmachineGun(
                    override val name: String,
                    override val description: String,
                    override val enhancements: List<Enhancement> = listOf(),
                    override val allowedLoadouts: List<LoadoutType> = listOf(LoadoutType.COMBAT),
                    override val damage: Damage = Damage.MEDIUM,
                    override val damageTypes: Set<DamageType> = setOf(DamageType.BALLISTIC),
                    override val magazine: Int = 32,
                    override val rateOfFire: Int = 8
                ) : Ranged(
                    name,
                    description,
                    enhancements,
                    allowedLoadouts,
                    damage,
                    Wield.TWO_HANDED,
                    damageTypes,
                    magazine,
                    rateOfFire
                )

                data class Shotgun(
                    override val name: String,
                    override val description: String,
                    override val enhancements: List<Enhancement> = listOf(),
                    override val allowedLoadouts: List<LoadoutType> = listOf(LoadoutType.COMBAT),
                    override val damage: Damage = Damage.MEDIUM,
                    override val damageTypes: Set<DamageType> = setOf(DamageType.BALLISTIC),
                    override val magazine: Int = 8,
                    override val rateOfFire: Int = 1
                ) : Ranged(
                    name,
                    description,
                    enhancements,
                    allowedLoadouts,
                    damage,
                    Wield.TWO_HANDED,
                    damageTypes,
                    magazine,
                    rateOfFire
                )

                data class MachineGun(
                    override val name: String,
                    override val description: String,
                    override val enhancements: List<Enhancement> = listOf(),
                    override val allowedLoadouts: List<LoadoutType> = listOf(LoadoutType.COMBAT),
                    override val damage: Damage = Damage.HEAVY,
                    override val damageTypes: Set<DamageType> = setOf(DamageType.BALLISTIC),
                    override val magazine: Int = 150,
                    override val rateOfFire: Int = 15
                ) : Ranged(
                    name,
                    description,
                    enhancements,
                    allowedLoadouts,
                    damage,
                    Wield.MOUNTED,
                    damageTypes,
                    magazine,
                    rateOfFire
                )

                data class AntimaterialGun(
                    override val name: String,
                    override val description: String,
                    override val enhancements: List<Enhancement> = listOf(),
                    override val allowedLoadouts: List<LoadoutType> = listOf(LoadoutType.COMBAT),
                    override val damage: Damage = Damage.HEAVY,
                    override val damageTypes: Set<DamageType> = setOf(DamageType.BALLISTIC),
                    override val magazine: Int = 5,
                    override val rateOfFire: Int = 1
                ) : Ranged(
                    name,
                    description,
                    enhancements,
                    allowedLoadouts,
                    damage,
                    Wield.MOUNTED,
                    damageTypes,
                    magazine,
                    rateOfFire
                )

                // endregion Firearms
            }

            enum class Wield {
                ONE_HANDED, TWO_HANDED, MOUNTED
            }
        }

        sealed class Armor(
            name: String,
            description: String,
            enhancements: List<Enhancement>,
            open val protections: Set<DamageType>,
        ) : Item(name, description, listOf(LoadoutType.COMBAT), enhancements) {

            data class None(
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = listOf(),
                override val protections: Set<DamageType> = setOf(),
            ) : Armor(name, description, enhancements, protections)

            data class Clothing(
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = LoadoutType.values().toList(),
                override val protections: Set<DamageType> = setOf(),
            ) : Armor(name, description, enhancements, protections)

            // region Fantasy Armor

//            data class PaddedArmor(
//                override val name: String,
//                override val description: String,
//                override val enhancements: List<Enhancement> = listOf(),
//                override val allowedLoadouts: List<LoadoutType> = LoadoutType.values().toList(),
//                override val protections: Set<DamageType> = setOf(DamageType.SLASH),
//            ) : Armor(name, description, enhancements, allowedLoadouts, protections)

//            data class Mail(
//                override val name: String,
//                override val description: String,
//                override val enhancements: List<Enhancement> = listOf(),
//                override val allowedLoadouts: List<LoadoutType> = LoadoutType.values().toList(),
//                override val protections: Set<DamageType> = setOf(
//                    DamageType.SLASH,
//                    DamageType.PIERCE
//                ),
//            ) : Armor(name, description, enhancements, allowedLoadouts, protections)

//            data class Plate(
//                override val name: String,
//                override val description: String,
//                override val enhancements: List<Enhancement> = listOf(),
//                override val allowedLoadouts: List<LoadoutType> = LoadoutType.values().toList(),
//                override val protections: Set<DamageType> = setOf(
//                    DamageType.SLASH,
//                    DamageType.PIERCE,
//                    DamageType.BLUNT
//                ),
//            ) : Armor(name, description, enhancements, allowedLoadouts, protections)

            // endregion Fantasy Armor

            // region Scifi Armor

            data class Kevlar(
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = LoadoutType.values().toList(),
                override val protections: Set<DamageType> = setOf(DamageType.BALLISTIC),
            ) : Armor(name, description, enhancements, protections)

            data class ExoSkeleton(
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = LoadoutType.values().toList(),
                override val protections: Set<DamageType> = setOf(
                    DamageType.BALLISTIC,
                    DamageType.SLASH,
                    DamageType.PIERCE
                ),
            ) : Armor(name, description, enhancements, protections)

            data class VacSuit(
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = LoadoutType.values().toList(),
                override val protections: Set<DamageType> = setOf(DamageType.BREATH),
            ) : Armor(name, description, enhancements, protections)

            data class VacArmor(
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = LoadoutType.values().toList(),
                override val protections: Set<DamageType> = setOf(
                    DamageType.BREATH,
                    DamageType.BALLISTIC,
                    DamageType.SLASH,
                    DamageType.PIERCE
                ),
            ) : Armor(name, description, enhancements, protections)

            data class PoweredArmor(
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(
                    Enhancement(
                        "Power Assisst",
                        "Added artificial strength"
                    )
                ),
                override val allowedLoadouts: List<LoadoutType> = LoadoutType.values().toList(),
                override val protections: Set<DamageType> = setOf(
                    DamageType.BREATH,
                    DamageType.BALLISTIC,
                    DamageType.BLUNT,
                    DamageType.SLASH,
                    DamageType.PIERCE
                ),
            ) : Armor(name, description, enhancements, protections)

            // endregion Scifi Armor
        }

        enum class Damage(val damage: Int) {
            LIGHT(1),
            MEDIUM(2),
            HEAVY(3);
        }

        data class Other(
            override val name: String,
            override val description: String,
            override val enhancements: List<Enhancement> = listOf()
        ) : Item(name, description, LoadoutType.values().toList(), enhancements)

        data class Enhancement(
            val name: String,
            val description: String
        )

        enum class DamageType(val mask: Int) {
            BLUNT(1),             // hammer
            SLASH(2),             // sword
            PIERCE(4),            // dagger, arrow, bullet
            MAGIC(8),             // duh
            ELECTRICITY(16),      // duh
            FIRE(32),             // duh
            BALLISTIC(64),        // bullet
            RADIATION(128),       // duh
            PARTICLE(256),        // laser
            BREATH(512),          // suffocation
            KINETIC(1024)         // explosions
        }

        enum class LoadoutType {
            COMBAT, SOCIAL, TRAVEL;

            val isPrimaryAllowed: Boolean
                get() = this.isOneOf(COMBAT)
            val isSecondaryAllowed: Boolean
                get() = this.isOneOf(COMBAT, SOCIAL, TRAVEL)
            val isTertiaryAllowed: Boolean
                get() = this.isOneOf(COMBAT, SOCIAL, TRAVEL)
            val isClothingAllowed: Boolean
                get() = this.isOneOf(SOCIAL, TRAVEL)
            val isArmorAllowed: Boolean
                get() = this.isOneOf(COMBAT)
        }
    }
}
package com.pecawolf.model

sealed class Item(
    open val itemId: Long,
    open val name: String,
    open val description: String,
    open val allowedLoadouts: List<LoadoutType>,
    open val enhancements: List<Enhancement> = listOf()
) {

    sealed class Weapon(
        itemId: Long,
        name: String,
        description: String,
        enhancements: List<Enhancement>,
        allowedLoadouts: List<LoadoutType>,
        open val damage: Damage,
        open val wield: Wield,
        open val damageTypes: Set<DamageType>,
    ) : Item(itemId, name, description, allowedLoadouts, enhancements) {

        sealed class Melee(
            itemId: Long,
            name: String,
            description: String,
            enhancements: List<Enhancement>,
            allowedLoadouts: List<LoadoutType>,
            damage: Damage,
            wield: Wield,
            damageTypes: Set<DamageType>,
        ) : Weapon(
            itemId,
            name,
            description,
            enhancements,
            allowedLoadouts,
            damage,
            wield,
            damageTypes
        ) {

            object BareHands : Melee(
                0,
                "",
                "",
                listOf(),
                LoadoutType.ALL,
                Damage.LIGHT,
                Wield.TWO_HANDED,
                setOf(DamageType.BLUNT)
            )

            data class Knife(
                override val itemId: Long,
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = LoadoutType.ALL,
                override val damage: Damage = Damage.LIGHT,
                override val damageTypes: Set<DamageType> = setOf(
                    DamageType.PIERCE,
                    DamageType.SLASH
                ),
            ) : Melee(
                itemId,
                name,
                description,
                enhancements,
                allowedLoadouts,
                damage,
                Wield.ONE_HANDED,
                damageTypes
            )

            data class Sword(
                override val itemId: Long,
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
                itemId,
                name,
                description,
                enhancements,
                allowedLoadouts,
                damage,
                wield,
                damageTypes
            )

            data class Axe(
                override val itemId: Long,
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
                itemId,
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
            itemId: Long,
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
            itemId,
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
                override val itemId: Long,
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = LoadoutType.ALL,
                override val damage: Damage = Damage.LIGHT,
                override val damageTypes: Set<DamageType> = setOf(DamageType.PIERCE),
                override val magazine: Int = 1,
                override val rateOfFire: Int = 1
            ) : Ranged(
                itemId,
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

            data class Crossbow(
                override val itemId: Long,
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = LoadoutType.ALL,
                override val damage: Damage = Damage.MEDIUM,
                override val damageTypes: Set<DamageType> = setOf(DamageType.PIERCE),
                override val magazine: Int = 1,
                override val rateOfFire: Int = 1
            ) : Ranged(
                itemId,
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
                override val itemId: Long,
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = LoadoutType.ALL,
                override val damage: Damage = Damage.LIGHT,
                override val damageTypes: Set<DamageType> = setOf(DamageType.BALLISTIC),
                override val magazine: Int = 12,
                override val rateOfFire: Int = 2
            ) : Ranged(
                itemId,
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
                override val itemId: Long,
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = LoadoutType.ALL,
                override val damage: Damage = Damage.LIGHT,
                override val damageTypes: Set<DamageType> = setOf(DamageType.BALLISTIC),
                override val magazine: Int = 6,
                override val rateOfFire: Int = 1
            ) : Ranged(
                itemId,
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
                override val itemId: Long,
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = listOf(LoadoutType.COMBAT),
                override val damage: Damage = Damage.MEDIUM,
                override val damageTypes: Set<DamageType> = setOf(DamageType.BALLISTIC),
                override val magazine: Int = 20,
                override val rateOfFire: Int = 5
            ) : Ranged(
                itemId,
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
                override val itemId: Long,
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = listOf(LoadoutType.COMBAT),
                override val damage: Damage = Damage.MEDIUM,
                override val damageTypes: Set<DamageType> = setOf(DamageType.BALLISTIC),
                override val magazine: Int = 32,
                override val rateOfFire: Int = 8
            ) : Ranged(
                itemId,
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
                override val itemId: Long,
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = listOf(LoadoutType.COMBAT),
                override val damage: Damage = Damage.MEDIUM,
                override val damageTypes: Set<DamageType> = setOf(DamageType.BALLISTIC),
                override val magazine: Int = 8,
                override val rateOfFire: Int = 1
            ) : Ranged(
                itemId,
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
                override val itemId: Long,
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = listOf(LoadoutType.COMBAT),
                override val damage: Damage = Damage.HEAVY,
                override val damageTypes: Set<DamageType> = setOf(DamageType.BALLISTIC),
                override val magazine: Int = 150,
                override val rateOfFire: Int = 15
            ) : Ranged(
                itemId,
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
                override val itemId: Long,
                override val name: String,
                override val description: String,
                override val enhancements: List<Enhancement> = listOf(),
                override val allowedLoadouts: List<LoadoutType> = listOf(LoadoutType.COMBAT),
                override val damage: Damage = Damage.HEAVY,
                override val damageTypes: Set<DamageType> = setOf(DamageType.BALLISTIC),
                override val magazine: Int = 5,
                override val rateOfFire: Int = 1
            ) : Ranged(
                itemId,
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
        itemId: Long,
        name: String,
        description: String,
        enhancements: List<Enhancement>,
        allowedLoadouts: List<LoadoutType> = listOf(),
        open val protections: Set<DamageType>,
    ) : Item(itemId, name, description, allowedLoadouts, enhancements) {

        object None : Armor(-1, "", "", listOf(), LoadoutType.ALL, setOf())

        data class Clothing(
            override val itemId: Long,
            override val name: String,
            override val description: String,
            override val enhancements: List<Enhancement> = listOf(),
            override val protections: Set<DamageType> = setOf(),
        ) : Armor(itemId, name, description, enhancements, LoadoutType.ALL, protections)

        data class Kevlar(
            override val itemId: Long,
            override val name: String,
            override val description: String,
            override val enhancements: List<Enhancement> = listOf(),
            override val allowedLoadouts: List<LoadoutType> = LoadoutType.ALL,
            override val protections: Set<DamageType> = setOf(DamageType.BALLISTIC),
        ) : Armor(itemId, name, description, enhancements, allowedLoadouts, protections)

        data class ExoSkeleton(
            override val itemId: Long,
            override val name: String,
            override val description: String,
            override val enhancements: List<Enhancement> = listOf(),
            override val allowedLoadouts: List<LoadoutType> = LoadoutType.ALL,
            override val protections: Set<DamageType> = setOf(
                DamageType.BALLISTIC,
                DamageType.SLASH,
                DamageType.PIERCE
            ),
        ) : Armor(itemId, name, description, enhancements, allowedLoadouts, protections)

        data class VacSuit(
            override val itemId: Long,
            override val name: String,
            override val description: String,
            override val enhancements: List<Enhancement> = listOf(),
            override val allowedLoadouts: List<LoadoutType> = LoadoutType.ALL,
            override val protections: Set<DamageType> = setOf(DamageType.BREATH),
        ) : Armor(itemId, name, description, enhancements, allowedLoadouts, protections)

        data class VacArmor(
            override val itemId: Long,
            override val name: String,
            override val description: String,
            override val enhancements: List<Enhancement> = listOf(),
            override val allowedLoadouts: List<LoadoutType> = LoadoutType.ALL,
            override val protections: Set<DamageType> = setOf(
                DamageType.BREATH,
                DamageType.BALLISTIC,
                DamageType.SLASH,
                DamageType.PIERCE
            ),
        ) : Armor(itemId, name, description, enhancements, allowedLoadouts, protections)

        data class PoweredArmor(
            override val itemId: Long,
            override val name: String,
            override val description: String,
            override val enhancements: List<Enhancement> = listOf(
                Enhancement(
                    "Power Assisst",
                    "Added artificial strength"
                )
            ),
            override val allowedLoadouts: List<LoadoutType> = LoadoutType.ALL,
            override val protections: Set<DamageType> = setOf(
                DamageType.BREATH,
                DamageType.BALLISTIC,
                DamageType.BLUNT,
                DamageType.SLASH,
                DamageType.PIERCE
            ),
        ) : Armor(itemId, name, description, enhancements, allowedLoadouts, protections)
    }

    enum class Damage(val damage: Int) {
        LIGHT(1),
        MEDIUM(2),
        HEAVY(3);
    }

    data class Other(
        override val itemId: Long,
        override val name: String,
        override val description: String,
        override val enhancements: List<Enhancement> = listOf()
    ) : Item(itemId, name, description, LoadoutType.ALL, enhancements)

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

        companion object {
            val ALL: List<LoadoutType>
                get() = values().toList()
        }
    }
}

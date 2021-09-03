package com.pecawolf.model

import com.pecawolf.common.extensions.isNotOneOf

sealed class Item(
    open var itemId: Long,
    open var name: String,
    open var description: String,
    open var count: Int,
    open var enhancements: List<Enhancement> = listOf(),
    open var allowedLoadouts: MutableList<LoadoutType>,
    open var damage: Damage,
    open var damageTypes: MutableSet<DamageType>,
) {

    val allowedSlots: List<Slot>
        get() = when (this) {
            is Armor.Clothing -> listOf(Slot.CLOTHING, Slot.ARMOR)
            is Armor -> listOf(Slot.ARMOR)
//            is Weapon.Grenade -> listOf(Slot.GRENADE)
            is Weapon -> listOf(Slot.PRIMARY, Slot.SECONDARY, Slot.TERTIARY)
            else -> listOf()
        }

    sealed class Weapon(
        itemId: Long,
        name: String,
        description: String,
        count: Int,
        enhancements: List<Enhancement>,
        allowedLoadouts: MutableList<LoadoutType>,
        damage: Damage,
        open var wield: Wield,
        damageTypes: MutableSet<DamageType>,
    ) : Item(itemId, name, description, count, enhancements, allowedLoadouts, damage, damageTypes) {

        sealed class Melee(
            itemId: Long,
            name: String,
            description: String,
            count: Int,
            enhancements: List<Enhancement>,
            allowedLoadouts: MutableList<LoadoutType>,
            damage: Damage,
            wield: Wield,
            damageTypes: MutableSet<DamageType>,
        ) : Weapon(
            itemId,
            name,
            description,
            count,
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
                1,
                listOf(),
                LoadoutType.ALL,
                Damage.LIGHT,
                Wield.TWO_HANDED,
                mutableSetOf(DamageType.BLUNT)
            )

            data class Knife(
                override var itemId: Long,
                override var name: String,
                override var description: String,
                override var count: Int,
                override var enhancements: List<Enhancement> = listOf(),
                override var allowedLoadouts: MutableList<LoadoutType> = LoadoutType.ALL,
                override var damage: Damage = Damage.LIGHT,
                override var damageTypes: MutableSet<DamageType> = mutableSetOf(
                    DamageType.PIERCE,
                    DamageType.SLASH
                ),
            ) : Melee(
                itemId,
                name,
                description,
                count,
                enhancements,
                allowedLoadouts,
                damage,
                Wield.ONE_HANDED,
                damageTypes
            )

            data class Sword(
                override var itemId: Long,
                override var name: String,
                override var description: String,
                override var count: Int,
                override var enhancements: List<Enhancement> = listOf(),
                override var allowedLoadouts: MutableList<LoadoutType> = mutableListOf(LoadoutType.COMBAT),
                override var damage: Damage = Damage.MEDIUM,
                override var wield: Wield = Wield.ONE_HANDED,
                override var damageTypes: MutableSet<DamageType> = mutableSetOf(
                    DamageType.PIERCE,
                    DamageType.SLASH
                ),
            ) : Melee(
                itemId,
                name,
                description,
                count,
                enhancements,
                allowedLoadouts,
                damage,
                wield,
                damageTypes
            )

            data class Axe(
                override var itemId: Long,
                override var name: String,
                override var description: String,
                override var count: Int,
                override var enhancements: List<Enhancement> = listOf(),
                override var allowedLoadouts: MutableList<LoadoutType> = mutableListOf(LoadoutType.COMBAT),
                override var damage: Damage = Damage.MEDIUM,
                override var wield: Wield = Wield.ONE_HANDED,
                override var damageTypes: MutableSet<DamageType> = mutableSetOf(
                    DamageType.SLASH,
                    DamageType.BLUNT
                ),
            ) : Melee(
                itemId,
                name,
                description,
                count,
                enhancements,
                allowedLoadouts,
                damage,
                wield,
                damageTypes
            )

            data class Hammer(
                override var itemId: Long,
                override var name: String,
                override var description: String,
                override var count: Int,
                override var enhancements: List<Enhancement> = listOf(),
                override var allowedLoadouts: MutableList<LoadoutType> = mutableListOf(LoadoutType.COMBAT),
                override var damage: Damage = Damage.MEDIUM,
                override var wield: Wield = Wield.ONE_HANDED,
                override var damageTypes: MutableSet<DamageType> = mutableSetOf(DamageType.BLUNT),
            ) : Melee(
                itemId,
                name,
                description,
                count,
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
            count: Int,
            enhancements: List<Enhancement>,
            allowedLoadouts: MutableList<LoadoutType>,
            damage: Damage,
            wield: Wield,
            damageTypes: MutableSet<DamageType>,
            open var magazine: Int,
            open var rateOfFire: Int
        ) : Weapon(
            itemId,
            name,
            description,
            count,
            enhancements,
            allowedLoadouts,
            damage,
            wield,
            damageTypes
        ) {

            // region Primitive Weapons

            data class Bow(
                override var itemId: Long,
                override var name: String,
                override var description: String,
                override var count: Int,
                override var enhancements: List<Enhancement> = listOf(),
                override var allowedLoadouts: MutableList<LoadoutType> = LoadoutType.ALL,
                override var damage: Damage = Damage.LIGHT,
                override var damageTypes: MutableSet<DamageType> = mutableSetOf(DamageType.PIERCE),
                override var magazine: Int = 1,
                override var rateOfFire: Int = 1
            ) : Ranged(
                itemId,
                name,
                description,
                count,
                enhancements,
                allowedLoadouts,
                damage,
                Wield.TWO_HANDED,
                damageTypes,
                magazine,
                rateOfFire
            )

            data class Crossbow(
                override var itemId: Long,
                override var name: String,
                override var description: String,
                override var count: Int,
                override var enhancements: List<Enhancement> = listOf(),
                override var allowedLoadouts: MutableList<LoadoutType> = LoadoutType.ALL,
                override var damage: Damage = Damage.MEDIUM,
                override var damageTypes: MutableSet<DamageType> = mutableSetOf(DamageType.PIERCE),
                override var magazine: Int = 1,
                override var rateOfFire: Int = 1
            ) : Ranged(
                itemId,
                name,
                description,
                count,
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
                override var itemId: Long,
                override var name: String,
                override var description: String,
                override var count: Int,
                override var enhancements: List<Enhancement> = listOf(),
                override var allowedLoadouts: MutableList<LoadoutType> = LoadoutType.ALL,
                override var damage: Damage = Damage.LIGHT,
                override var damageTypes: MutableSet<DamageType> = mutableSetOf(DamageType.BALLISTIC),
                override var magazine: Int = 12,
                override var rateOfFire: Int = 2
            ) : Ranged(
                itemId,
                name,
                description,
                count,
                enhancements,
                allowedLoadouts,
                damage,
                Wield.ONE_HANDED,
                damageTypes,
                magazine,
                rateOfFire
            )

            data class Revolver(
                override var itemId: Long,
                override var name: String,
                override var description: String,
                override var count: Int,
                override var enhancements: List<Enhancement> = listOf(),
                override var allowedLoadouts: MutableList<LoadoutType> = LoadoutType.ALL,
                override var damage: Damage = Damage.LIGHT,
                override var damageTypes: MutableSet<DamageType> = mutableSetOf(DamageType.BALLISTIC),
                override var magazine: Int = 6,
                override var rateOfFire: Int = 1
            ) : Ranged(
                itemId,
                name,
                description,
                count,
                enhancements,
                allowedLoadouts,
                damage,
                Wield.ONE_HANDED,
                damageTypes,
                magazine,
                rateOfFire
            )

            data class Rifle(
                override var itemId: Long,
                override var name: String,
                override var description: String,
                override var count: Int,
                override var enhancements: List<Enhancement> = listOf(),
                override var allowedLoadouts: MutableList<LoadoutType> = mutableListOf(LoadoutType.COMBAT),
                override var damage: Damage = Damage.MEDIUM,
                override var damageTypes: MutableSet<DamageType> = mutableSetOf(DamageType.BALLISTIC),
                override var magazine: Int = 20,
                override var rateOfFire: Int = 5
            ) : Ranged(
                itemId,
                name,
                description,
                count,
                enhancements,
                allowedLoadouts,
                damage,
                Wield.TWO_HANDED,
                damageTypes,
                magazine,
                rateOfFire
            )

            data class SubmachineGun(
                override var itemId: Long,
                override var name: String,
                override var description: String,
                override var count: Int,
                override var enhancements: List<Enhancement> = listOf(),
                override var allowedLoadouts: MutableList<LoadoutType> = mutableListOf(LoadoutType.COMBAT),
                override var damage: Damage = Damage.MEDIUM,
                override var damageTypes: MutableSet<DamageType> = mutableSetOf(DamageType.BALLISTIC),
                override var magazine: Int = 32,
                override var rateOfFire: Int = 8
            ) : Ranged(
                itemId,
                name,
                description,
                count,
                enhancements,
                allowedLoadouts,
                damage,
                Wield.TWO_HANDED,
                damageTypes,
                magazine,
                rateOfFire
            )

            data class Shotgun(
                override var itemId: Long,
                override var name: String,
                override var description: String,
                override var count: Int,
                override var enhancements: List<Enhancement> = listOf(),
                override var allowedLoadouts: MutableList<LoadoutType> = mutableListOf(LoadoutType.COMBAT),
                override var damage: Damage = Damage.MEDIUM,
                override var damageTypes: MutableSet<DamageType> = mutableSetOf(DamageType.BALLISTIC),
                override var magazine: Int = 8,
                override var rateOfFire: Int = 1
            ) : Ranged(
                itemId,
                name,
                description,
                count,
                enhancements,
                allowedLoadouts,
                damage,
                Wield.TWO_HANDED,
                damageTypes,
                magazine,
                rateOfFire
            )

            data class MachineGun(
                override var itemId: Long,
                override var name: String,
                override var description: String,
                override var count: Int,
                override var enhancements: List<Enhancement> = listOf(),
                override var allowedLoadouts: MutableList<LoadoutType> = mutableListOf(LoadoutType.COMBAT),
                override var damage: Damage = Damage.HEAVY,
                override var damageTypes: MutableSet<DamageType> = mutableSetOf(DamageType.BALLISTIC),
                override var magazine: Int = 150,
                override var rateOfFire: Int = 15
            ) : Ranged(
                itemId,
                name,
                description,
                count,
                enhancements,
                allowedLoadouts,
                damage,
                Wield.MOUNTED,
                damageTypes,
                magazine,
                rateOfFire
            )

            data class AntimaterialGun(
                override var itemId: Long,
                override var name: String,
                override var description: String,
                override var count: Int,
                override var enhancements: List<Enhancement> = listOf(),
                override var allowedLoadouts: MutableList<LoadoutType> = mutableListOf(LoadoutType.COMBAT),
                override var damage: Damage = Damage.HEAVY,
                override var damageTypes: MutableSet<DamageType> = mutableSetOf(DamageType.BALLISTIC),
                override var magazine: Int = 5,
                override var rateOfFire: Int = 1
            ) : Ranged(
                itemId,
                name,
                description,
                count,
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

        data class Grenade(
            override var itemId: Long,
            override var name: String,
            override var description: String,
            override var count: Int,
            override var enhancements: List<Enhancement>,
            override var damage: Damage,
            override var damageTypes: MutableSet<DamageType>,
        ) : Weapon(
            itemId,
            name,
            description,
            count,
            enhancements,
            mutableListOf(LoadoutType.COMBAT),
            damage,
            Wield.ONE_HANDED,
            damageTypes
        )

        enum class Wield {
            ONE_HANDED, TWO_HANDED, MOUNTED //, DRONE
        }
    }

    sealed class Armor(
        itemId: Long,
        name: String,
        description: String,
        count: Int,
        enhancements: List<Enhancement>,
        allowedLoadouts: MutableList<LoadoutType> = mutableListOf(),
        damage: Damage,
        damageTypes: MutableSet<DamageType>,
    ) : Item(itemId, name, description, count, enhancements, allowedLoadouts, damage, damageTypes) {

        object None : Armor(-1, "", "", 0, listOf(), LoadoutType.ALL, Damage.NONE, mutableSetOf())

        data class Clothing(
            override var itemId: Long,
            override var name: String,
            override var description: String,
            override var count: Int,
            override var enhancements: List<Enhancement> = listOf(),
            override var damageTypes: MutableSet<DamageType> = mutableSetOf(),
        ) : Armor(
            itemId,
            name,
            description,
            count,
            enhancements,
            LoadoutType.ALL,
            Damage.NONE,
            damageTypes
        )

        data class Kevlar(
            override var itemId: Long,
            override var name: String,
            override var description: String,
            override var count: Int,
            override var enhancements: List<Enhancement> = listOf(),
            override var allowedLoadouts: MutableList<LoadoutType> = LoadoutType.ALL,
            override var damage: Damage,
            override var damageTypes: MutableSet<DamageType> = mutableSetOf(DamageType.BALLISTIC),
        ) : Armor(
            itemId,
            name,
            description,
            count,
            enhancements,
            allowedLoadouts,
            damage,
            damageTypes
        )

        data class ExoSkeleton(
            override var itemId: Long,
            override var name: String,
            override var description: String,
            override var count: Int,
            override var enhancements: List<Enhancement> = listOf(),
            override var allowedLoadouts: MutableList<LoadoutType> = LoadoutType.ALL,
            override var damage: Damage,
            override var damageTypes: MutableSet<DamageType> = mutableSetOf(
                DamageType.BALLISTIC,
                DamageType.SLASH,
                DamageType.PIERCE
            ),
        ) : Armor(
            itemId,
            name,
            description,
            count,
            enhancements,
            allowedLoadouts,
            damage,
            damageTypes
        )

        data class VacSuit(
            override var itemId: Long,
            override var name: String,
            override var description: String,
            override var count: Int,
            override var enhancements: List<Enhancement> = listOf(),
            override var allowedLoadouts: MutableList<LoadoutType> = LoadoutType.ALL,
            override var damage: Damage,
            override var damageTypes: MutableSet<DamageType> = mutableSetOf(DamageType.BREATH),
        ) : Armor(
            itemId,
            name,
            description,
            count,
            enhancements,
            allowedLoadouts,
            damage,
            damageTypes
        )

        data class VacArmor(
            override var itemId: Long,
            override var name: String,
            override var description: String,
            override var count: Int,
            override var enhancements: List<Enhancement> = listOf(),
            override var allowedLoadouts: MutableList<LoadoutType> = LoadoutType.ALL,
            override var damage: Damage,
            override var damageTypes: MutableSet<DamageType> = mutableSetOf(
                DamageType.BREATH,
                DamageType.BALLISTIC,
                DamageType.SLASH,
                DamageType.PIERCE
            ),
        ) : Armor(
            itemId,
            name,
            description,
            count,
            enhancements,
            allowedLoadouts,
            damage,
            damageTypes
        )

        data class PoweredArmor(
            override var itemId: Long,
            override var name: String,
            override var description: String,
            override var count: Int,
            override var enhancements: List<Enhancement> = listOf(
                Enhancement(
                    "Power Assisst",
                    "Added artificial strength"
                )
            ),
            override var allowedLoadouts: MutableList<LoadoutType> = LoadoutType.ALL,
            override var damage: Damage,
            override var damageTypes: MutableSet<DamageType> = mutableSetOf(
                DamageType.BREATH,
                DamageType.BALLISTIC,
                DamageType.BLUNT,
                DamageType.SLASH,
                DamageType.PIERCE
            ),
        ) : Armor(
            itemId,
            name,
            description,
            count,
            enhancements,
            allowedLoadouts,
            damage,
            damageTypes
        )
    }

    enum class Damage(var damage: Int) {
        NONE(0),
        LIGHT(1),
        MEDIUM(2),
        HEAVY(3);
    }

    data class Electronics(
        override var itemId: Long,
        override var name: String,
        override var description: String,
        override var count: Int,
        override var enhancements: List<Enhancement> = listOf()
    ) : Item(
        itemId,
        name,
        description,
        count,
        enhancements,
        LoadoutType.ALL,
        Damage.NONE,
        mutableSetOf()
    )

    data class Food(
        override var itemId: Long,
        override var name: String,
        override var description: String,
        override var count: Int,
        override var enhancements: List<Enhancement> = listOf()
    ) : Item(
        itemId,
        name,
        description,
        count,
        enhancements,
        LoadoutType.ALL,
        Damage.NONE,
        mutableSetOf()
    )

    data class Potion(
        override var itemId: Long,
        override var name: String,
        override var description: String,
        override var count: Int,
        override var enhancements: List<Enhancement> = listOf()
    ) : Item(
        itemId,
        name,
        description,
        count,
        enhancements,
        LoadoutType.ALL,
        Damage.NONE,
        mutableSetOf()
    )

    data class Other(
        override var itemId: Long,
        override var name: String,
        override var description: String,
        override var count: Int,
        override var enhancements: List<Enhancement> = listOf()
    ) : Item(
        itemId,
        name,
        description,
        count,
        enhancements,
        LoadoutType.ALL,
        Damage.NONE,
        mutableSetOf()
    )

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
            val ALL: MutableList<LoadoutType>
                get() = values().toMutableList()
        }
    }

    enum class ItemType(
        val isWeapon: Boolean,
        val isRanged: Boolean,
        val isArmor: Boolean,
        val defaultWield: Weapon.Wield?,
        val defaultDamage: Damage,
        val defaultDamageTypes: MutableSet<DamageType>
    ) {
        BARE_HANDS(true, false, false, null, Damage.LIGHT, mutableSetOf(DamageType.BLUNT)),
        KNIFE(
            true,
            false,
            false,
            Weapon.Wield.ONE_HANDED,
            Damage.LIGHT,
            mutableSetOf(DamageType.SLASH, DamageType.PIERCE)
        ),
        SWORD(
            true,
            false,
            false,
            Weapon.Wield.ONE_HANDED,
            Damage.LIGHT,
            mutableSetOf(DamageType.SLASH, DamageType.PIERCE)
        ),
        AXE(
            true,
            false,
            false,
            Weapon.Wield.ONE_HANDED,
            Damage.LIGHT,
            mutableSetOf(DamageType.BLUNT, DamageType.SLASH)
        ),
        HAMMER(
            true,
            false,
            false,
            Weapon.Wield.ONE_HANDED,
            Damage.LIGHT,
            mutableSetOf(DamageType.BLUNT, DamageType.SLASH)
        ),
        BOW(
            true,
            true,
            false,
            Weapon.Wield.TWO_HANDED,
            Damage.LIGHT,
            mutableSetOf(DamageType.PIERCE)
        ),
        CROSSBOW(
            true,
            true,
            false,
            Weapon.Wield.TWO_HANDED,
            Damage.MEDIUM,
            mutableSetOf(DamageType.PIERCE)
        ),
        PISTOL(
            true,
            true,
            false,
            Weapon.Wield.ONE_HANDED,
            Damage.LIGHT,
            mutableSetOf(DamageType.BALLISTIC)
        ),
        REVOLVER(
            true,
            true,
            false,
            Weapon.Wield.ONE_HANDED,
            Damage.LIGHT,
            mutableSetOf(DamageType.BALLISTIC)
        ),
        RIFLE(
            true,
            true,
            false,
            Weapon.Wield.TWO_HANDED,
            Damage.MEDIUM,
            mutableSetOf(DamageType.BALLISTIC)
        ),
        SUBMACHINE_GUN(
            true,
            true,
            false,
            Weapon.Wield.TWO_HANDED,
            Damage.MEDIUM,
            mutableSetOf(DamageType.BALLISTIC)
        ),
        SHOTGUN(
            true,
            true,
            false,
            Weapon.Wield.TWO_HANDED,
            Damage.MEDIUM,
            mutableSetOf(DamageType.BALLISTIC)
        ),
        MACHINE_GUN(
            true,
            true,
            false,
            Weapon.Wield.TWO_HANDED,
            Damage.HEAVY,
            mutableSetOf(DamageType.BALLISTIC)
        ),
        ANTIMATERIAL_GUN(
            true,
            true,
            false,
            Weapon.Wield.TWO_HANDED,
            Damage.HEAVY,
            mutableSetOf(DamageType.BALLISTIC)
        ),

        GRENADE(
            false,
            false,
            false,
            null,
            Damage.HEAVY,
            mutableSetOf(DamageType.FIRE, DamageType.KINETIC, DamageType.PIERCE)
        ),

        NONE(false, false, true, null, Damage.NONE, mutableSetOf()),
        CLOTHING(false, false, true, null, Damage.NONE, mutableSetOf()),
        KEVLAR(false, false, true, null, Damage.LIGHT, mutableSetOf(DamageType.BALLISTIC)),
        EXO_SKELETON(
            false,
            false,
            true,
            null,
            Damage.MEDIUM,
            mutableSetOf(
                DamageType.BALLISTIC,
                DamageType.BLUNT,
                DamageType.SLASH,
                DamageType.PIERCE
            )
        ),
        VAC_SUIT(false, false, true, null, Damage.LIGHT, mutableSetOf(DamageType.BREATH)),
        VAC_ARMOR(
            false,
            false,
            true,
            null,
            Damage.MEDIUM,
            mutableSetOf(
                DamageType.BREATH,
                DamageType.BALLISTIC,
                DamageType.SLASH,
                DamageType.PIERCE
            )
        ),
        POWERED_ARMOR(
            false,
            false,
            true,
            null,
            Damage.HEAVY,
            mutableSetOf(
                DamageType.BREATH,
                DamageType.BALLISTIC,
                DamageType.BLUNT,
                DamageType.KINETIC,
                DamageType.RADIATION
            )
        ),

        ELECTRONICS(false, false, false, null, Damage.NONE, mutableSetOf()),
        FOOD(false, false, false, null, Damage.NONE, mutableSetOf()),
        POTION(false, false, false, null, Damage.NONE, mutableSetOf()),
        OTHER(false, false, false, null, Damage.NONE, mutableSetOf());

        companion object {
            fun items() = values().toList().filter { it.isNotOneOf(NONE, BARE_HANDS) }
        }
    }

    enum class Slot {
        PRIMARY,
        SECONDARY,
        TERTIARY,

        //        GRENADE,
        ARMOR,
        CLOTHING;
    }
}

package cz.pecawolf.charactersheet.common.model

data class Equipment(
    val primary: Item.Weapon,
    val secondary: Item.Weapon,
    val tertiary: Item.Weapon,
    val clothes: Item.Armor,
    val armor: Item.Armor
) {

    sealed class Item(
        open val name: String,
        open val description: String
    ) {

        sealed class Weapon(
            name: String,
            description: String,
            open val isCivilian: Boolean,
            open val damageTypes: Set<DamageType>
        ) : Item(name, description) {

            sealed class Melee(
                override val name: String,
                override val description: String,
                override val isCivilian: Boolean,
                override val damageTypes: Set<DamageType>
            ) : Weapon(name, description, isCivilian, damageTypes) {

                data class Knife(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = true,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.SLASH,
                        DamageType.PIERCE
                    )
                ) : Melee(name, description, isCivilian, damageTypes)

                data class Sword(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = true,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.SLASH,
                        DamageType.PIERCE,
                        DamageType.BLUNT
                    )
                ) : Melee(name, description, isCivilian, damageTypes)

                data class LargeSword(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = false,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.SLASH,
                        DamageType.PIERCE,
                        DamageType.BLUNT
                    )
                ) : Melee(name, description, isCivilian, damageTypes)

                data class Axe(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = true,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.SLASH,
                        DamageType.BLUNT
                    )
                ) : Melee(name, description, isCivilian, damageTypes)

                data class LargeAxe(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = false,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.SLASH,
                        DamageType.BLUNT
                    )
                ) : Melee(name, description, isCivilian, damageTypes)

                data class Hammer(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = true,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.BLUNT,
                        DamageType.PIERCE
                    )
                ) : Melee(name, description, isCivilian, damageTypes)

                data class LargeHammer(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = false,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.BLUNT,
                        DamageType.PIERCE
                    )
                ) : Melee(name, description, isCivilian, damageTypes)

                data class Polearm(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = false,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.SLASH,
                        DamageType.PIERCE,
                        DamageType.BLUNT
                    )
                ) : Melee(name, description, isCivilian, damageTypes)
            }

            sealed class Ranged(
                name: String,
                description: String,
                override val isCivilian: Boolean,
                override val damageTypes: Set<DamageType>
            ) : Weapon(name, description, isCivilian, damageTypes) {

                data class Throwable(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = false,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.PIERCE,
                        DamageType.BLUNT
                    )
                ) : Ranged(name, description, isCivilian, damageTypes)

                data class Javelin(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = false,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.PIERCE
                    )
                ) : Ranged(name, description, isCivilian, damageTypes)

                data class Bow(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = false,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.PIERCE
                    )
                ) : Ranged(name, description, isCivilian, damageTypes)

                data class Crossbow(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = false,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.PIERCE
                    )
                ) : Ranged(name, description, isCivilian, damageTypes)

                data class Pistol(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = true,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.BALLISTIC
                    )
                ) : Ranged(name, description, isCivilian, damageTypes)

                data class Revolver(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = true,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.BALLISTIC
                    )
                ) : Ranged(name, description, isCivilian, damageTypes)

                data class Rifle(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = false,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.BALLISTIC,
                        DamageType.PARTICLE
                    )
                ) : Ranged(name, description, isCivilian, damageTypes)

                data class SubMachineGun(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = false,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.BALLISTIC,
                        DamageType.PARTICLE
                    )
                ) : Ranged(name, description, isCivilian, damageTypes)

                data class MachineGun(
                    override val name: String,
                    override val description: String,
                    override val isCivilian: Boolean = false,
                    override val damageTypes: Set<DamageType> = setOf(
                        DamageType.BALLISTIC,
                        DamageType.PARTICLE
                    )
                ) : Ranged(name, description, isCivilian, damageTypes)
            }
        }

        sealed class Armor(
            name: String,
            description: String,
            open val protections: Set<DamageType>
        ) : Item(name, description) {

            data class Clothing(
                override val name: String,
                override val description: String,
                override val protections: Set<DamageType> = setOf()
            ) : Armor(name, description, protections)

            data class PaddedArmor(
                override val name: String,
                override val description: String,
                override val protections: Set<DamageType> = setOf(DamageType.SLASH)
            ) : Armor(name, description, protections)

            data class Chainmail(
                override val name: String,
                override val description: String,
                override val protections: Set<DamageType> = setOf(
                    DamageType.SLASH,
                    DamageType.PIERCE
                )
            ) : Armor(name, description, protections)

            data class Brigantine(
                override val name: String,
                override val description: String,
                override val protections: Set<DamageType> = setOf(
                    DamageType.SLASH,
                    DamageType.PIERCE,
                    DamageType.BLUNT
                )
            ) : Armor(name, description, protections)

            data class Plate(
                override val name: String,
                override val description: String,
                override val protections: Set<DamageType> = setOf(
                    DamageType.SLASH,
                    DamageType.PIERCE,
                    DamageType.BLUNT
                )
            ) : Armor(name, description, protections)

            data class Kevlar(
                override val name: String,
                override val description: String,
                override val protections: Set<DamageType> = setOf(
                    DamageType.SLASH,
                    DamageType.PIERCE,
                    DamageType.BLUNT,
                    DamageType.BALLISTIC
                )
            ) : Armor(name, description, protections)

            data class VacSuit(
                override val name: String,
                override val description: String,
                override val protections: Set<DamageType> = setOf(DamageType.BREATH)
            ) : Armor(name, description, protections)

            data class VacArmor(
                override val name: String,
                override val description: String,
                override val protections: Set<DamageType> = setOf(
                    DamageType.BREATH,
                    DamageType.SLASH,
                    DamageType.PIERCE,
                    DamageType.BLUNT,
                    DamageType.BALLISTIC
                )
            ) : Armor(name, description, protections)

            data class ExoSkeleton(
                override val name: String,
                override val description: String,
                override val protections: Set<DamageType> = setOf(
                    DamageType.BREATH,
                    DamageType.SLASH,
                    DamageType.PIERCE,
                    DamageType.BLUNT,
                    DamageType.BALLISTIC,
                    DamageType.RADIATION,
                    DamageType.KINETIC
                )
            ) : Armor(name, description, protections)
        }

        data class Other(
            override val name: String,
            override val description: String
        ) : Item(name, description)
    }

    enum class DamageType(val mask: Int) {
        BLUNT(1),             // hammer
        SLASH(2),             // sword
        PIERCE(4),            // dagger
        MAGIC(8),             // duh
        ELECTRICITY(16),      // duh
        FIRE(32),             // duh
        BALLISTIC(64),        // bullet
        RADIATION(128),       // duh
        PARTICLE(256),        // laser
        BREATH(512),          // suffocation
        KINETIC(1024)         // explosions
    }
}
package com.pecawolf.model

data class Inventory(
    val money: Int = 0,
    val primary: Item.Weapon = Item.Weapon.Melee.BareHands,
    val secondary: Item.Weapon = Item.Weapon.Melee.BareHands,
    val tertiary: Item.Weapon = Item.Weapon.Melee.BareHands,
    val clothes: Item.Armor = Item.Armor.None,
    val armor: Item.Armor = Item.Armor.None,
    val backpack: List<Item> = listOf(),
)
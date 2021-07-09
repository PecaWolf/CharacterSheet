package com.pecawolf.data.mapper

interface BaseMapper<MODEL, ENTITY, AD_MODEL, AD_ENTITY> {

    fun fromEntity(entity: ENTITY, additional: List<AD_ENTITY> = listOf()): MODEL

    fun toEntity(model: MODEL, additional: List<AD_MODEL> = listOf()): ENTITY
}
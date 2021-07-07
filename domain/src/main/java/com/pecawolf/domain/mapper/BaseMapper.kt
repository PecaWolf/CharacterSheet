package com.pecawolf.domain.mapper

interface BaseMapper<MODEL, RESPONSE, ENTITY> {

    fun fromResponse(response: RESPONSE): MODEL

    fun fromEntity(entity: ENTITY): MODEL

    fun toEntity(model: MODEL): ENTITY
}
package com.pecawolf.data.mapper

interface BaseMapper<MODEL, RESPONSE, ENTITY> {

    fun fromResponse(response: RESPONSE): MODEL

    fun fromEntity(entity: ENTITY): MODEL

    fun toEntity(model: MODEL): ENTITY
}
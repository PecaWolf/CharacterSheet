package com.pecawolf.common.exception

class CharacterNotFoundException(id: Long?) : Exception("$id")
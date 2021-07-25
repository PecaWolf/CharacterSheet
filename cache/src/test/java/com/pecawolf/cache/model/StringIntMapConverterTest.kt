package com.pecawolf.cache.model

import junit.framework.Assert.assertEquals
import org.junit.Test

class StringIntMapConverterTest {

    val converter = StringIntMapConverter()

    @Test
    fun `when `() {
        val fromString = converter.fromString("BCKS:1")
        assertEquals("BCKS", fromString.entries.first().key)
        assertEquals(1, fromString.entries.first().value)
    }
}
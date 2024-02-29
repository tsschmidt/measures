package com.tsschmi.measures

import kotlin.test.Test
import kotlin.test.assertEquals

class TemperatureTest {

    @Test
    fun base() {
        assertEquals(33.8, Celsius(1.0).base, 0.0001)
    }

    @Test
    fun fahrenheit() {
        assertEquals(1.0, Celsius(-17.2222).fahrenheit, 0.0001)
    }

    @Test
    fun getCelsius() {
        assertEquals(1.0, Fahrenheit(33.8).celsius, 0.0001)
    }
}
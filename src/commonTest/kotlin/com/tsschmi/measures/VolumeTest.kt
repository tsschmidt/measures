package com.tsschmi.measures

import kotlin.test.Test
import kotlin.test.assertEquals

class VolumeTest {

    @Test
    fun base() {
        assertEquals(3.78541, Gallon(1.0).base, 0.0001)
        assertEquals(0.473176, Pint(1.0).base, 0.0001)
        assertEquals(0.24, Cup(1.0).base, 0.0001)
        assertEquals(0.0147868, Tablespoon(1.0).base, 0.0001)
        assertEquals(0.00492892, Teaspoon(1.0).base, 0.0001)
        assertEquals(0.946353, Quart(1.0).base, 0.0001)
        assertEquals(0.001, Milliliter(1.0).base, 0.0001)
        assertEquals(0.0295735, FluidOunce(1.0).base, 0.0001)
    }

    @Test
    fun liter() {
        assertEquals(1.0, Quart(1.05669).liter, 0.0001)
        assertEquals(1.0, Gallon(0.264172).liter, 0.0001)
        assertEquals(1.0, Pint(2.11338).liter, 0.0001)
        assertEquals(1.0, Cup(4.16667).liter, 0.0001)
        assertEquals(1.0, Tablespoon(67.628).liter, 0.0001)
        assertEquals(1.0, Teaspoon(202.884).liter, 0.0001)
        assertEquals(1.0, Milliliter(1000.0).base, 0.0001)
        assertEquals(1.0, FluidOunce(33.814).liter, 0.0001)
    }

    @Test
    fun milliliter() {
        assertEquals(1.0, Quart(0.00105669).milliliter, 0.0001)
        assertEquals(1.0, Gallon(0.000264172).milliliter, 0.0001)
        assertEquals(1.0, Pint(0.00211338).milliliter, 0.0001)
        assertEquals(1.0, Cup(0.00416667).milliliter, 0.0001)
        assertEquals(1.0, Tablespoon(0.067628).milliliter, 0.0001)
        assertEquals(1.0, Teaspoon(0.202884).milliliter, 0.0001)
        assertEquals(1.0, FluidOunce(0.033814).milliliter, 0.0001)
        assertEquals(1.0, Liter(0.001).milliliter, 0.0001)
    }

    @Test
    fun gallon() {
        assertEquals(1.0, Quart(4.0).gallon, 0.0001)
        assertEquals(1.0, Milliliter(3785.41).gallon, 0.0001)
        assertEquals(1.0, Pint(8.0).gallon, 0.0001)
        assertEquals(1.0, Cup(15.7725).gallon, 0.0001)
        assertEquals(1.0, Tablespoon(256.0).gallon, 0.0001)
        assertEquals(1.0, Teaspoon(768.0).gallon, 0.0001)
        assertEquals(1.0, FluidOunce(128.0).gallon, 0.0001)
        assertEquals(1.0, Liter(3.78541).gallon, 0.0001)
    }

    @Test
    fun quart() {
        assertEquals(1.0, Gallon(0.25).quart, 0.0001)
        assertEquals(1.0, Milliliter(946.353).quart, 0.0001)
        assertEquals(1.0, Pint(2.0).quart, 0.0001)
        assertEquals(1.0, Cup(3.94314).quart, 0.0001)
        assertEquals(1.0, Tablespoon(64.0).quart, 0.0001)
        assertEquals(1.0, Teaspoon(192.0).quart, 0.0001)
        assertEquals(1.0, FluidOunce(32.0).quart, 0.0001)
        assertEquals(1.0, Liter(0.946353).quart, 0.0001)
    }

    @Test
    fun pint() {
        assertEquals(1.0, Gallon(0.125).pint, 0.0001)
        assertEquals(1.0, Milliliter(473.176).pint, 0.0001)
        assertEquals(1.0, Quart(0.5).pint, 0.0001)
        assertEquals(1.0, Cup(1.97157).pint, 0.0001)
        assertEquals(1.0, Tablespoon(32.0).pint, 0.0001)
        assertEquals(1.0, Teaspoon(96.0).pint, 0.0001)
        assertEquals(1.0, FluidOunce(16.0).pint, 0.0001)
        assertEquals(1.0, Liter(0.473176).pint, 0.0001)
    }

    @Test
    fun cup() {
        assertEquals(1.0, Gallon(0.0634013).cup, 0.0001)
        assertEquals(1.0, Milliliter(240.0).cup, 0.0001)
        assertEquals(1.0, Quart(0.253605).cup, 0.0001)
        assertEquals(1.0, Pint(0.50721).cup, 0.0001)
        assertEquals(1.0, Tablespoon(16.2307).cup, 0.0001)
        assertEquals(1.0, Teaspoon(48.6922).cup, 0.0001)
        assertEquals(1.0, FluidOunce(8.11537).cup, 0.0001)
        assertEquals(1.0, Liter(0.24).cup, 0.0001)
    }

    @Test
    fun tablespoon() {
        assertEquals(1.0, Gallon(0.00390625).tablespoon, 0.0001)
        assertEquals(1.0, Milliliter(14.7868).tablespoon, 0.0001)
        assertEquals(1.0, Quart(0.015625).tablespoon, 0.0001)
        assertEquals(1.0, Cup(0.0616115).tablespoon, 0.0001)
        assertEquals(1.0, Pint(0.03125).tablespoon, 0.0001)
        assertEquals(1.0, Teaspoon(3.0).tablespoon, 0.0001)
        assertEquals(1.0, FluidOunce(0.5).tablespoon, 0.0001)
        assertEquals(1.0, Liter(0.0147868).tablespoon, 0.0001)
    }

    @Test
    fun teaspoon() {
        assertEquals(1.0, Gallon(0.00130208).teaspoon, 0.0001)
        assertEquals(1.0, Milliliter(4.92892).teaspoon, 0.0001)
        assertEquals(1.0, Quart(0.00520833).teaspoon, 0.0001)
        assertEquals(1.0, Cup(0.0205372).teaspoon, 0.0001)
        assertEquals(1.0, Pint(0.0104167).teaspoon, 0.0001)
        assertEquals(1.0, Tablespoon(0.333333).teaspoon, 0.0001)
        assertEquals(1.0, FluidOunce(0.166667).teaspoon, 0.0001)
        assertEquals(1.0, Liter(0.00492892).teaspoon, 0.0001)
    }

    @Test
    fun fluidOunce() {
        assertEquals(1.0, Gallon(0.0078125).fluidOunce, 0.0001)
        assertEquals(1.0, Milliliter(29.5735).fluidOunce, 0.0001)
        assertEquals(1.0, Quart(0.03125).fluidOunce, 0.0001)
        assertEquals(1.0, Cup(0.123223).fluidOunce, 0.0001)
        assertEquals(1.0, Pint(0.0625).fluidOunce, 0.0001)
        assertEquals(1.0, Tablespoon(2.0).fluidOunce, 0.0001)
        assertEquals(1.0, Teaspoon(6.0).fluidOunce, 0.0001)
        assertEquals(1.0, Liter(0.0295735).fluidOunce, 0.0001)
    }
}
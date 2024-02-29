package com.tsschmi.measures

import kotlin.test.Test
import kotlin.test.assertEquals

class GravityTest {

    @Test
    fun base() {
        assertEquals(40.0, SpecificGravity(1.040).base, 0.0001)
        assertEquals(40.0, Brix(10.0).base, 0.1)
        assertEquals(40.0, Plato(10.0).base, 0.1)
        assertEquals(40.0, Yield(86.95).base, 0.1)
    }

    @Test
    fun sg() {
        assertEquals(1.040, GravityPoint(40.0).specificGravity, 0.0001)
        assertEquals(1.040, Brix(10.0).specificGravity, 0.1)
        assertEquals(1.040, Plato(10.0).specificGravity, 0.1)
        assertEquals(1.040, Yield(86.95).specificGravity, 0.1)
    }

    @Test
    fun brix() {
        assertEquals(10.0, GravityPoint(40.0).brix, 0.1)
        assertEquals(10.0, SpecificGravity(1.040).brix, 0.1)
        assertEquals(10.0, Plato(10.0).brix, 0.1)
        assertEquals(10.0, Yield(86.95).brix, 0.1)
    }

    @Test
    fun getPlato() {
        assertEquals(10.0, GravityPoint(40.0).plato, 0.1)
        assertEquals(10.0, SpecificGravity(1.040).plato, 0.1)
        assertEquals(10.0, Brix(10.0).plato, 0.1)
        assertEquals(10.0, Yield(86.95).plato, 0.1)
    }

    @Test
    fun getYield() {
        assertEquals(86.95, GravityPoint(40.0).yield, 0.1)
        assertEquals(86.95, SpecificGravity(1.040).yield, 0.1)
        assertEquals(86.95, Plato(10.0).yield, 0.1)
        assertEquals(86.95, Brix(10.0).yield, 0.1)
    }

}
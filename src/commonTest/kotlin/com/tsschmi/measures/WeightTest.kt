package com.tsschmi.measures
import com.tsschmi.measures.Weight.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.asserter

class WeightTest {

    @Test
    fun testKilogram() {
        val k = Kilogram(1.0)
        assertEquals("1.00kg", k.display(2))
        assertEquals(2.20462, k.pound, .00001)
        assertEquals(35.274, k.ounce, 0.001)
        assertEquals(1000.0, k.gram)
        val kt = k + k
        assertEquals(2.0, kt())
        val k3 = k - k
        assertEquals(0.0, k3())
        val k4 = kt * kt
        assertEquals(4.0, k4(), 0.0001)
        val k5 = kt.pound / kt.pound
        assertEquals(1.0, k5)
        assertEquals(16.0, (k4 * k4)(), 0.0001)
        assertEquals(2.0, (Kilogram(6.0) % Kilogram(4.0))())
        assertEquals(2.0, (Kilogram(1.0) + Pound(2.20462))(), 0.0001)
        assertEquals(2.0, (Kilogram(1.0) + Ounce(35.274))(), 0.0001)
        assertEquals(2.0, (Kilogram(1.0) + Gram(1000.0))(), 0.0001)
        assertEquals(3.0, (Kilogram(1.0) + Gram(1000.0) + Pound(2.20462))(), 0.0001)
        assertEquals(2.0, (Pound(1.0) + Ounce(16.0))())
    }

    /*
    @Test
    fun getBase() {
    }

    @Test
    fun getKilogram() {
    }

    @Test
    fun getGram() {
    }

    @Test
    fun getPound() {
    }

    @Test
    fun getOunce() {
    }

    @Test
    fun toKilograms() {
    }

    @Test
    fun toGram() {
    }

    @Test
    fun toPound() {
    }

    @Test
    fun toOunce() {
    }

    @Test
    fun testToString() {
    }

    @Test
    fun getType() {
    }
     */
}
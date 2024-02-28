package com.tsschmi.measures
import com.tsschmi.measures.Weight.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test

class WeightTest {

    @Test
    fun testEquals() {
        val k: Weight = Kilogram(1.0)
        val k1 = k + Pound(2.0)

        val k2 = (2.0 * Kilogram(2.0))(WeightType.PoundType)
        val k3 = 3.0(WeightType.KilogramType)

        val eq = 2.0 * ((k - k1) / k2())
        println(k2.display(2))
        println("${k1(WeightType.PoundType)()}, ${k1.gram}, ${k1.ounce}, ${k1.pound}")
    }

    @Test
    fun testSerialization() {
        val sg: Weight = Pound(1.0)
        val json = Json.encodeToString(sg)
        println(json)
        val des: Weight = Json.decodeFromString(json)
        println(des.display(2))
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
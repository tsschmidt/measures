package com.tsschmi.measures
import com.tsschmi.measures.Kilogram
import com.tsschmi.measures.Pound
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test

class WeightTest {

    @Test
    fun testEquals() {
        val k = Kilogram(1.0)
        val k1: Kilogram = k + Kilogram(2.0)
        println("${k1()}, ${k1.gram}, ${k1.ounce}, ${k1.pound}")
    }

    @Test
    fun testSerialization() {
        val sg: Pound = Pound(1.0)
        val json = Json.encodeToString(sg)
        println(json)
        val des: Pound = Json.decodeFromString(json)
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
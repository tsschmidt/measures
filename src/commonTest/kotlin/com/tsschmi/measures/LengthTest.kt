package com.tsschmi.measures

import com.tsschmi.measures.Centimeter
import com.tsschmi.measures.Inch
import kotlin.test.Test

class LengthTest {

    @Test
    fun testLength() {
        println(Centimeter(1.0).inches)
        println(Inch(1.0).cm)
    }
    /*
    @Test
    fun getBase() {
    }

    @Test
    fun getInches() {
    }

    @Test
    fun getCm() {
    }

    @Test
    fun getMeter() {
    }

    @Test
    fun getFeet() {
    }

    @Test
    fun toInches() {
    }

    @Test
    fun toCM() {
    }

    @Test
    fun toMeter() {
    }

    @Test
    fun toFeet() {
    }

    @Test
    fun testToString() {
    }

    @Test
    fun getType() {
    }
     */
}
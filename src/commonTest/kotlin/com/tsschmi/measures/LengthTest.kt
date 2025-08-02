package com.tsschmi.measures

import kotlin.test.Test
import kotlin.test.assertEquals

class LengthTest {

    @Test
    fun base() {
        assertEquals(0.0254, Inch(1.0).base, 0.0001)
        assertEquals(0.3048, Foot(1.0).base, 0.0001)
        assertEquals(0.01, Centimeter(1.0).base, 0.0001)
        assertEquals(1000.0, Kilometer(1.0).base, 0.0001)
        assertEquals(1609.34, Mile(1.0).base, 0.0001)
        assertEquals(0.9144, Yard(1.0).base, 0.0001)
    }

    @Test
    fun inches() {
        assertEquals(1.0, Foot(0.0833333).inches, 0.0001)
        assertEquals(1.0, Centimeter(2.54).inches, 0.0001)
        assertEquals(1.0, Meter(0.0254).inches, 0.0001)
        assertEquals(1.0, Kilometer(2.54e-5).inches, 0.0001)
        assertEquals(1.0, Mile(1.5783e-5).inches, 0.0001)
        assertEquals(1.0, Yard(0.0277778).inches, 0.0001)
    }

    @Test
    fun centimeter() {
        assertEquals(1.0, Foot(0.032808).cm, 0.0001)
        assertEquals(1.0, Inch(0.393701).cm, 0.0001)
        assertEquals(1.0, Meter(0.01).cm, 0.0001)
        assertEquals(1.0, Kilometer(1e-5).cm, 0.0001)
        assertEquals(1.0, Mile(6.2137e-6).cm, 0.0001)
        assertEquals(1.0, Yard(0.0109361).cm, 0.0001)
    }

    @Test
    fun meter() {
        assertEquals(1.0, Foot(3.28084).meter, 0.0001)
        assertEquals(1.0, Inch(39.3701).meter, 0.0001)
        assertEquals(1.0, Centimeter(100.0).meter, 0.0001)
        assertEquals(1.0, Kilometer(0.001).meter, 0.0001)
        assertEquals(1.0, Mile(0.000621371).meter, 0.0001)
        assertEquals(1.0, Yard(1.09361).meter, 0.0001)
    }

    @Test
    fun foot() {
        assertEquals(1.0, Meter(0.3048).feet, 0.0001)
        assertEquals(1.0, Inch(12.0).feet, 0.0001)
        assertEquals(1.0, Centimeter(30.48).feet, 0.0001)
        assertEquals(1.0, Kilometer(0.0003048).feet, 0.0001)
        assertEquals(1.0, Mile(0.000189394).feet, 0.0001)
        assertEquals(1.0, Yard(0.333333).feet, 0.0001)
    }

    @Test
    fun yard() {
        assertEquals(1.0, Meter(0.9144).yard, 0.0001)
        assertEquals(1.0, Inch(36.0).yard, 0.0001)
        assertEquals(1.0, Centimeter(91.44).yard, 0.0001)
        assertEquals(1.0, Kilometer(0.0009144).yard, 0.0001)
        assertEquals(1.0, Mile(0.000568182).yard, 0.0001)
        assertEquals(1.0, Foot(3.0).yard, 0.0001)
    }

    @Test
    fun mile() {
        assertEquals(1.0, Meter(1609.34).mile, 0.0001)
        assertEquals(1.0, Inch(63360.0).mile, 0.0001)
        assertEquals(1.0, Centimeter(160934.0).mile, 0.0001)
        assertEquals(1.0, Kilometer(1.60934).mile, 0.0001)
        assertEquals(1.0, Yard(1760.0).mile, 0.0001)
        assertEquals(1.0, Foot(5280.0).mile, 0.0001)
    }

    @Test
    fun kilometer() {
        assertEquals(1.0, Meter(1000.0).kilometer, 0.0001)
        assertEquals(1.0, Inch(39370.1).kilometer, 0.0001)
        assertEquals(1.0, Centimeter(100000.0).kilometer, 0.0001)
        assertEquals(1.0, Mile(0.621371).kilometer, 0.0001)
        assertEquals(1.0, Yard(1093.61).kilometer, 0.0001)
        assertEquals(1.0, Foot(3280.84).kilometer, 0.0001)
    }
}
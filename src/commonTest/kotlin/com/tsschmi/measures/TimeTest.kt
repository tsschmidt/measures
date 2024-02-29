package com.tsschmi.measures

import kotlin.test.Test
import kotlin.test.assertEquals

class TimeTest {

    @Test
    fun base() {
        assertEquals(60.0, Minute(1.0).base, 0.001)
        assertEquals(3600.0, Hour(1.0).base, 0.01)
        assertEquals(86400.0, Day(1.0).base, 0.0001)
        assertEquals(604800.0, Week(1.0).base, 0.0001)
        assertEquals(2.628e+6, Month(1.0).base, 0.0001)
        assertEquals(SECONDS_IN_A_YEAR, Year(1.0).base, 0.0001)
    }

    @Test
    fun seconds() {
        assertEquals(1.0, Minute(0.0166667).seconds, 0.0001)
        assertEquals(1.0, Hour(0.000277778).seconds, 0.0001)
        assertEquals(1.0, Day(1.1574e-5).seconds, 0.0001)
        assertEquals(1.0, Week(1.6534e-6).seconds, 0.0001)
        assertEquals(1.0, Month(3.8052e-7).seconds, 0.0001)
        assertEquals(1.0, Year(3.171e-8).seconds, 0.0001)
    }

    @Test
    fun minutes() {
        assertEquals(1.0, Second(60.0).minutes, 0.0001)
        assertEquals(1.0, Hour(0.0166667).minutes, 0.0001)
        assertEquals(1.0, Day(0.000694444).minutes, 0.0001)
        assertEquals(1.0, Week(9.9206e-5).minutes, 0.0001)
        assertEquals(1.0, Month(2.2831e-5).minutes, 0.0001)
        assertEquals(1.0, Year(1.9026e-6).minutes, 0.0001)
    }

    @Test
    fun hours() {
        assertEquals(1.0, Second(3600.0).hours, 0.0001)
        assertEquals(1.0, Minute(60.0).hours, 0.0001)
        assertEquals(1.0, Day(0.0416667).hours, 0.0001)
        assertEquals(1.0, Week(0.00595238).hours, 0.0001)
        assertEquals(1.0, Month(0.00136986).hours, 0.0001)
        assertEquals(1.0, Year(0.000114155).hours, 0.0001)
    }

    @Test
    fun days() {
        assertEquals(1.0, Second(86400.0).days, 0.0001)
        assertEquals(1.0, Minute(1440.0).days, 0.0001)
        assertEquals(1.0, Hour(24.0).days, 0.0001)
        assertEquals(1.0, Week(0.142857).days, 0.0001)
        assertEquals(1.0, Month(0.0328767).days, 0.0001)
        assertEquals(1.0, Year(0.00273973).days, 0.0001)
    }

    @Test
    fun weeks() {
        assertEquals(1.0, Second(604800.0).weeks, 0.0001)
        assertEquals(1.0, Minute(10080.0).weeks, 0.0001)
        assertEquals(1.0, Hour(168.0).weeks, 0.0001)
        assertEquals(1.0, Day(7.0).weeks, 0.0001)
        assertEquals(1.0, Month(0.230137).weeks, 0.0001)
        assertEquals(1.0, Year(0.0191781).weeks, 0.0001)
    }

    @Test
    fun months() {
        assertEquals(1.0, Second(2.628e+6).months, 0.0001)
        assertEquals(1.0, Minute(43800.0).months, 0.0001)
        assertEquals(1.0, Hour(730.001).months, 0.0001)
        assertEquals(1.0, Day(30.4167).months, 0.0001)
        assertEquals(1.0, Week(4.34524).months, 0.0001)
        assertEquals(1.0, Year(0.0833334).months, 0.0001)
    }

    @Test
    fun years() {
        assertEquals(1.0, Second(3.154e+7).years, 0.001)
        assertEquals(1.0, Minute(525600.0).years, 0.0001)
        assertEquals(1.0, Hour(8760.0).years, 0.0001)
        assertEquals(1.0, Day(365.0).years, 0.0001)
        assertEquals(1.0, Week(52.1429).years, 0.0001)
        assertEquals(1.0, Month(12.0).years, 0.0001)
    }

}
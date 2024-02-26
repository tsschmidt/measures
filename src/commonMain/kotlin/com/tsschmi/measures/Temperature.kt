@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import com.tsschmi.measures.TemperatureType.*

@JsExport
@Serializable
sealed class TemperatureType(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double
) : MeasureType<Temperature> {

    @Serializable
    object FAHRENHEIT : TemperatureType("\u00B0F", identity, identity) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Fahrenheit(v) as T
    }

    @Serializable
    object CELSIUS : TemperatureType("\u00B0C", ctoF, fToC) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Celsius(v) as T
    }
}

/**
 * Abstract base class extended by specific Temperature unit classes.
 */
@Serializable
@JsExport
sealed class Temperature : BaseMeasure(), Comparable<Temperature> {
    /* Properties used to access this Temperature's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val fahrenheit by lazy { FAHRENHEIT.fromBase(base) }
    val celsius by lazy { CELSIUS.fromBase(base) }

    /** Returns the Temperature's value in seconds in a new [Fahrenheit] instance. */
    @Suppress("UNUSED")
    fun toFahrenheit() = Fahrenheit(fahrenheit)

    /** Returns the Temperature's value in seconds in a new [Celsius] instance. */
    @Suppress("UNUSED")
    fun toCelsius() = Celsius(celsius)

    override fun compareTo(other: Temperature): Int = base.compareTo(other.base)

    override fun equals(other: Any?) = other != null && other is Temperature && base == other.base

    override fun hashCode(): Int = Temperature::class.hashCode() * 31 + base.hashCode()

    operator fun <T : Temperature> plus(o: Temperature) : T = type.create(type.fromBase(base + o.base))

    operator fun <T : Temperature> minus(o: Temperature): T = type.create(type.fromBase(base - o.base))

    operator fun <T : Temperature> times(o: Temperature): T = type.create(type.fromBase(base * o.base))

    operator fun <T : Temperature> div(o: Temperature): T = type.create(type.fromBase(base / o.base))

    operator fun <T : Temperature> rem(o: Temperature): T = type.create(type.fromBase(base % o.base))

    operator fun <T : Temperature> unaryPlus(): T = type.create(-value)

    operator fun <T : Temperature> unaryMinus(): T = type.create(+value)

    operator fun <T : Temperature> inc(): T = type.create(value + 1.0)

    operator fun <T : Temperature> dec(): T = type.create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : Temperature> plus(v: Double): T = type.create(value + v)

    @JsName("minusDouble")
    operator fun <T : Temperature> minus(v: Double): T = type.create(value - v)

    @JsName("timesDouble")
    operator fun <T : Temperature> times(v: Double): T = type.create(value * v)

    @JsName("divDouble")
    operator fun <T : Temperature> div(v: Double): T = type.create(value / v)

    @JsName("remDouble")
    operator fun <T : Temperature> rem(v: Double): T = type.create(value % v)
}

/**
 * Class representing [Temperature] in Fahrenheit.
 */
@Serializable
@SerialName("fahrenheit")
@JsExport
class Fahrenheit(override val value: Double = 0.0) : Temperature() {
    override val type = FAHRENHEIT
}

/**
 * Class representing [Temperature] in Celsius.
 */
@Serializable
@SerialName("celsius")
@JsExport
class Celsius(override val value: Double = 0.0) : Temperature() {
    override val type = CELSIUS
}

/** Functions for converting [Temperature] values form base units to other units. */
val ctoF = { v: Double -> (v * 9.0 / 5.0) + 32.0 }
val fToC = { v: Double -> (v - 32.0) * 5.0 / 9.0 }

@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import com.tsschmi.measures.TemperatureType.*
import kotlin.js.JsName

@JsExport
@Serializable
sealed class TemperatureType<out T : Temperature>(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double,
    override val create: (Double) -> T
) : MeasureType<T> {
    @Serializable
    data object FahrenheitType : TemperatureType<Fahrenheit>("\u00B0F", identity, identity, ::Fahrenheit)

    @Serializable
    data object CelsiusType : TemperatureType<Celsius>("\u00B0C", ctoF, fToC, ::Celsius)
}

/**
 * Abstract base class extended by specific Temperature unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Temperature : Measure, Operators<Temperature>, Comparable<Temperature> {
    /* Properties used to access this Temperature's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val fahrenheit by lazy { FahrenheitType.fromBase(base) }
    val celsius by lazy { CelsiusType.fromBase(base) }

    override fun compareTo(other: Temperature): Int = base.compareTo(other.base)

    override fun equals(other: Any?) = other != null && other is Temperature && base == other.base

    override fun hashCode(): Int = Temperature::class.hashCode() * 31 + base.hashCode()

    @JsName("convert")
    operator fun <T : Temperature> invoke(t: TemperatureType<T>)  = t.create(t.fromBase(base))

    /** inc() and dec() implemented here instead of interface because of a Kotlin generics issue */
    @JsExport.Ignore
    operator fun inc() = this(value + 1.0)

    @JsExport.Ignore
    operator fun dec() = this(value - 1.0)
}

/**
 * Class representing [Temperature] in Fahrenheit.
 */
@Serializable
@SerialName("fahrenheit")
@JsExport
class Fahrenheit(override val value: Double = 0.0) : Temperature(), MeasureOperators<Fahrenheit, Temperature> {
    override val type = FahrenheitType

}

/**
 * Class representing [Temperature] in Celsius.
 */
@Serializable
@SerialName("celsius")
@JsExport
class Celsius(override val value: Double = 0.0) : Temperature(), MeasureOperators<Celsius, Temperature> {
    override val type = CelsiusType

}

/** Functions for converting [Temperature] values form base units to other units. */
val ctoF = { v: Double -> (v * 9.0 / 5.0) + 32.0 }
val fToC = { v: Double -> (v - 32.0) * 5.0 / 9.0 }

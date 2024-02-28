@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import com.tsschmi.measures.TemperatureType.*

@JsExport
@Serializable
sealed class TemperatureType(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double
) : MeasureType {
    @Serializable
    data object FahrenheitType : TemperatureType("\u00B0F", identity, identity)

    @Serializable
    data object CelsiusType : TemperatureType("\u00B0C", ctoF, fToC)
}

/**
 * Abstract base class extended by specific Temperature unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Temperature : Measure, Comparable<Temperature> {
    /* Properties used to access this Temperature's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val fahrenheit by lazy { FahrenheitType.fromBase(base) }
    val celsius by lazy { CelsiusType.fromBase(base) }

    override fun compareTo(other: Temperature): Int = base.compareTo(other.base)

    override fun equals(other: Any?) = other != null && other is Temperature && base == other.base

    override fun hashCode(): Int = Temperature::class.hashCode() * 31 + base.hashCode()
}

/**
 * Class representing [Temperature] in Fahrenheit.
 */
@Serializable
@SerialName("fahrenheit")
@JsExport
class Fahrenheit(override val value: Double = 0.0) : Temperature(), MeasureOperators<Fahrenheit, Temperature> {
    override val type: MeasureType = FahrenheitType
    override fun create(v: Double) = Fahrenheit(v)

}

/**
 * Class representing [Temperature] in Celsius.
 */
@Serializable
@SerialName("celsius")
@JsExport
class Celsius(override val value: Double = 0.0) : Temperature(), MeasureOperators<Celsius, Temperature> {
    override val type: MeasureType = CelsiusType
    override fun create(v: Double) = Celsius(v)

}

/** Functions for converting [Temperature] values form base units to other units. */
val ctoF = { v: Double -> (v * 9.0 / 5.0) + 32.0 }
val fToC = { v: Double -> (v - 32.0) * 5.0 / 9.0 }

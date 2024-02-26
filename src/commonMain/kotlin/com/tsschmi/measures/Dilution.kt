@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

/**
 * Enum implementing [MeasureType] to provide units of [Dilution] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Dilution] value in the common base units.
 * @param fromBase - Function that returns the [Dilution] value in the configured units from the base unit.
 */
@JsExport
@Serializable
sealed class DilutionType(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double
) : MeasureType<Dilution> {

    @Serializable
    data object PartsPerMillionType : DilutionType("ppm", identity, identity) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = PartsPerMillion(v) as T
    }
}

/**
 * Abstract base class extended by specific Gravity unit classes.
 */
@Serializable
@JsExport
sealed class Dilution : BaseMeasure(), Comparable<Dilution> {
    //abstract fun <T : Dilution> create(value: Double): T = PPM(value) as T
    override val base by lazy { type.toBase(value) }

    override fun equals(other: Any?) = other != null && other is Dilution && base == other.base

    override fun hashCode(): Int = Dilution::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Dilution): Int = base.compareTo(other.base)

    operator fun <T : Dilution> plus(o: Dilution) : T = type.create(type.fromBase(base + o.base))

    operator fun <T : Dilution> minus(o: Dilution): T = type.create(type.fromBase(base - o.base))

    operator fun <T : Dilution> times(o: Dilution): T = type.create(type.fromBase(base * o.base))

    operator fun <T : Dilution> div(o: Dilution): T = type.create(type.fromBase(base / o.base))

    operator fun <T : Dilution> rem(o: Dilution): T = type.create(type.fromBase(base % o.base))

    operator fun <T : Dilution> unaryPlus(): T = type.create(-value)

    operator fun <T : Dilution> unaryMinus(): T = type.create(+value)

    operator fun <T : Dilution> inc(): T = type.create(value + 1.0)

    operator fun <T : Dilution> dec(): T = type.create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : Dilution> plus(v: Double): T = type.create(value + v)

    @JsName("minusDouble")
    operator fun <T : Dilution> minus(v: Double): T = type.create(value - v)

    @JsName("timesDouble")
    operator fun <T : Dilution> times(v: Double): T = type.create(value * v)

    @JsName("divDouble")
    operator fun <T : Dilution> div(v: Double): T = type.create(value / v)

    @JsName("remDouble")
    operator fun <T : Dilution> rem(v: Double): T = type.create(value % v)
}

/**
 * Class representing a [Dilution] value in parts per million.
 */
@Serializable
@SerialName("ppm")
@JsExport
class PartsPerMillion(override val value: Double = 0.0) : Dilution() {
    override val type = DilutionType.PartsPerMillionType
}


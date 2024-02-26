@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import com.tsschmi.measures.GravityType.*

/**
 * Enum implementing [MeasureType] to provide units of [Gravity] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Gravity] value in the common base units.
 * @param fromBase - Function that returns the [Gravity] value in the configured units from the base unit.
 */
@JsExport
@Serializable
sealed class GravityType(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double
) : MeasureType<Gravity> {

    @Serializable
    data object GravityPointType : GravityType("ppg", identity, identity) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = GravityPoint(v) as T
    }

    @Serializable
    data object SpecificGravityType : GravityType("sg", sgToGp, gpToSg) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = SpecificGravity(v) as T
    }

    @Serializable
    data object BrixType : GravityType("brix", brixToGp, gpToBrix) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Brix(v) as T
    }

    @Serializable
    data object PlatoType : GravityType("P", platoToGp, gpToPlato) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Plato(v) as T
    }

    @Serializable
    data object YieldType : GravityType("%", yieldToGp, gpToYield) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Yield(v) as T
    }
}

/**
 * Abstract base class extended by specific Gravity unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Gravity : BaseMeasure(), Comparable<Gravity> {
    /* Properties used to access this Gravity's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val gravityPoints by lazy { GravityPointType.fromBase(base) }
    val specificGravity by lazy { SpecificGravityType.fromBase(base) }
    val brix by lazy { BrixType.fromBase(base) }
    val plato by lazy { PlatoType.fromBase(base) }
    val yield by lazy { YieldType.fromBase(base) }

    override fun equals(other: Any?) = other != null && other is Gravity && base == other.base

    override fun hashCode(): Int = Gravity::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Gravity): Int = base.compareTo(other.base)

    operator fun <T : Gravity> plus(o: Gravity) : T = type.create(type.fromBase(base + o.base))

    operator fun <T : Gravity> minus(o: Gravity): T = type.create(type.fromBase(base - o.base))

    operator fun <T : Gravity> times(o: Gravity): T = type.create(type.fromBase(base * o.base))

    operator fun <T : Gravity> div(o: Gravity): T = type.create(type.fromBase(base / o.base))

    operator fun <T : Gravity> rem(o: Gravity): T = type.create(type.fromBase(base % o.base))

    operator fun <T : Gravity> unaryPlus(): T = type.create(-value)

    operator fun <T : Gravity> unaryMinus(): T = type.create(+value)

    operator fun <T : Gravity> inc(): T = type.create(value + 1.0)

    operator fun <T : Gravity> dec(): T = type.create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : Gravity> plus(v: Double): T = type.create(value + v)

    @JsName("minusDouble")
    operator fun <T : Gravity> minus(v: Double): T = type.create(value - v)

    @JsName("timesDouble")
    operator fun <T : Gravity> times(v: Double): T = type.create(value * v)

    @JsName("divDouble")
    operator fun <T : Gravity> div(v: Double): T = type.create(value / v)

    @JsName("remDouble")
    operator fun <T : Gravity> rem(v: Double): T = type.create(value % v)

    @JsName("convert")
    operator fun <T : Gravity> invoke(type: GravityType): T = type.create(type.fromBase(base))
}

/**
 * Class representing [Gravity] in gravity points.
 */
@Serializable
@SerialName("gp")
@JsExport
class GravityPoint(override val value: Double = 0.0) : Gravity() {
    override val type = GravityPointType
}

/**
 * Class representing [Gravity] in brix.
 */
@Serializable
@SerialName("brix")
@JsExport
class Brix(override val value: Double = 0.0) : Gravity() {
    override val type = BrixType
}

/**
 * Class representing [Gravity] in specific gravity.
 */
@Serializable
@SerialName("sg")
@JsExport
class SpecificGravity(override val value: Double = 0.0) : Gravity() {
    override val type = SpecificGravityType

    /**
     * Overridden toString method to display specific gravity to 4 digits significance.
     */
    override fun toString() = format(4)
}

/**
 * Class representing [Gravity] in degrees plato.
 */
@Serializable
@SerialName("plato")
@JsExport
class Plato(override val value: Double = 0.0) : Gravity() {
    override val type = PlatoType
}

/**
 * Class representing [Gravity] in % yield of malt.
 */
@Serializable
@SerialName("yield")
@JsExport
class Yield(override val value: Double = 0.0) : Gravity() {
    override val type = YieldType
}

/** Functions to convert [Gravity] values from base unit to other units */
val gpToSg = { v: Double -> if (v > 0.0) (v / 1000) + 1.0 else 0.0 }
val gpToBrix = {v: Double -> gpToSg(v).let { (182.4601 * it * it * it) - (775.6821 * it * it) + (1262.7794 * it) - 669.5622 } }
val gpToYield = { v: Double -> v / 46.0 * 100.0 }
val gpToPlato = {v: Double -> gpToSg(v).let { (135.997 * it * it * it) - (630.272 * it * it) + (1111.14 * it) - 616.868 } }
val sgToGp = { sg: Double -> (sg - 1.0) * 1000 }
val brixToGp = { v: Double -> sgToGp((v / (258.6 - ((v / 258.2) * 227.1))) + 1) }
val platoToGp = { v: Double -> sgToGp((v / (258.6 - ((v / 258.2) * 227.1))) + 1) }
val yieldToGp = { yld: Double -> 46.0 * yld / 100.0 }

@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import com.tsschmi.measures.GravityType.*
import kotlin.js.JsName

/**
 * Enum implementing [MeasureType] to provide units of [Gravity] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Gravity] value in the common base units.
 * @param fromBase - Function that returns the [Gravity] value in the configured units from the base unit.
 */
@JsExport
@Serializable
sealed class GravityType<out T : Gravity>(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double,
    override val create: (Double) -> T
) : MeasureType<T> {
    @Serializable
    data object GravityPointType : GravityType<GravityPoint>("ppg", identity, identity, ::GravityPoint)

    @Serializable
    data object SpecificGravityType : GravityType<SpecificGravity>("sg", sgToGp, gpToSg, ::SpecificGravity)

    @Serializable
    data object BrixType : GravityType<Gravity>("brix", brixToGp, gpToBrix, ::Brix)

    @Serializable
    data object PlatoType : GravityType<Plato>("P", platoToGp, gpToPlato, ::Plato)

    @Serializable
    data object YieldType : GravityType<Yield>("%", yieldToGp, gpToYield, ::Yield)
}

/**
 * Abstract base class extended by specific Gravity unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Gravity : Measure, Operators<Gravity>, Comparable<Gravity> {
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

    @JsName("convert")
    operator fun <T : Gravity> invoke(g: GravityType<T>) = g.create(g.fromBase(base))
}


/**
 * Class representing [Gravity] in gravity points.
 */
@Serializable
@SerialName("gp")
@JsExport
class GravityPoint(override val value: Double = 0.0) : Gravity(), MeasureOperators<GravityPoint, Gravity> {
    override val type= GravityPointType
}

/**
 * Class representing [Gravity] in brix.
 */
@Serializable
@SerialName("brix")
@JsExport
class Brix(override val value: Double = 0.0) : Gravity(), MeasureOperators<Brix, Gravity> {
    override val type = BrixType
}

/**
 * Class representing [Gravity] in specific gravity.
 */
@Serializable
@SerialName("sg")
@JsExport
class SpecificGravity(override val value: Double = 0.0) : Gravity(), MeasureOperators<SpecificGravity, Gravity> {
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
class Plato(override val value: Double = 0.0) : Gravity(), MeasureOperators<Plato, Gravity> {
    override val type = PlatoType
}

/**
 * Class representing [Gravity] in % yield of malt.
 */
@Serializable
@SerialName("yield")
@JsExport
class Yield(override val value: Double = 0.0) : Gravity(), MeasureOperators<Yield, Gravity> {
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

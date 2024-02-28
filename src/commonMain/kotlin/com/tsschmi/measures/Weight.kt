@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import com.tsschmi.measures.WeightType.*

/**
 * Enum used to designate the unit of [Weight] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Weight] value in the common base units.
 * @param fromBase - Function that returns the [Weight] value in the configured units from the base unit.
 */
@JsExport
@Serializable
sealed class WeightType(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double
): MeasureType {
    @Serializable
    data object KilogramType : WeightType("kg", kgToLb, lbToKg)

    @Serializable
    data object GramType : WeightType("g", gToLb, lbToG)

    @Serializable
    data object PoundType : WeightType("lb", identity, identity)

    @Serializable
    data object OunceType : WeightType("oz", ozToLb, lbToOz)
}

/**
 * Abstract base class extended by specific Weight unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Weight : Measure, Amount, Comparable<Weight> {

    /* Properties used to access this Weight's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val kilogram by lazy { KilogramType.fromBase(base) }
    val gram by lazy { GramType.fromBase(base) }
    val pound by lazy { PoundType.fromBase(base) }
    val ounce by lazy { OunceType.fromBase(base) }

    /**
     * Default toString to display value with 2 significant digits without units.
     */
    //override fun toString() = format(2)

    override fun equals(other: Any?) = other != null && other is Weight && base == other.base

    override fun hashCode() =  Weight::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Weight): Int = base.compareTo(other.base)

    //@JsName("convert")
    //operator fun <T: Weight> invoke(type: WeightType): T = create(type.fromBase(base))
}

/**
 * Class that represents a [Weight] in kilograms.
 *
 * @param value - Double value for a [Weight] in units of Kilograms
 */
@Serializable
@SerialName("kilogram")
@JsExport
class Kilogram(override val value: Double = 0.0) : Weight(), MeasureOperators<Kilogram, Weight> {
    override val type: MeasureType = KilogramType
    override fun create(v: Double) = Kilogram(v)
}

/**
 * Class that represents a [Weight] in Grams.
 *
 * @param value - Double value for a [Weight] in units of grams
 */
@Serializable
@SerialName("gram")
@JsExport
class Gram(override val value: Double = 0.0) : Weight(), MeasureOperators<Gram, Weight> {
    override val type: MeasureType = GramType
    override fun create(v: Double) = Gram(v)
}

/**
 * Class that represents a [Weight] in pounds.
 *
 * @param value - Double value for a [Weight] in units of pounds.
 */
@Serializable
@SerialName("pound")
@JsExport
class Pound(override val value: Double = 0.0) : Weight(), MeasureOperators<Pound, Weight> {
    override val type: MeasureType = KilogramType
    override fun create(v: Double) = Pound(v)
}

/**
 * Class that represents a [Weight] in ounces.
 *
 * @param value - Double value for a [Weight] in units of ounces.
 */
@Serializable
@SerialName("ounce")
@JsExport
class Ounce(override val value: Double = 0.0): Weight(), MeasureOperators<Ounce, Weight> {
    override val type: MeasureType = OunceType
    override fun create(v: Double) = Ounce(v)
}

/* Constants used for converting from base unit(pounds) to other units */
const val POUND_KILOGRAM = 0.453592
const val POUND_GRAM = 453.592
const val POUND_OUNCE = 0.0625

/* Functions used for converting values between Weight units to the base unit(pounds) */
val lbToKg = { v: Double -> v * POUND_KILOGRAM }
val kgToLb = { v: Double -> v / POUND_KILOGRAM }
val lbToG = { v: Double -> v * POUND_GRAM }
val gToLb = { v: Double -> v / POUND_GRAM }
val ozToLb = { v: Double -> v * POUND_OUNCE }
val lbToOz = { v: Double -> v / POUND_OUNCE }
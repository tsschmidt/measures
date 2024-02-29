@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import com.tsschmi.measures.WeightType.*
import kotlin.js.JsName

/**
 * Enum used to designate the unit of [Weight] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Weight] value in the common base units.
 * @param fromBase - Function that returns the [Weight] value in the configured units from the base unit.
 */
@JsExport
@Serializable
sealed class WeightType<out T : Weight>(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double,
    override val create: (Double) -> T
): MeasureType<T> {
    @Serializable
    data object KilogramType : WeightType<Kilogram>("kg", toBase(POUND_IN_KILOGRAM), fromBase(POUND_IN_KILOGRAM), ::Kilogram)

    @Serializable
    data object GramType : WeightType<Gram>("g", toBase(POUND_IN_GRAM), fromBase(POUND_IN_GRAM), ::Gram)

    @Serializable
    data object PoundType : WeightType<Pound>("lb", identity, identity, ::Pound)

    @Serializable
    data object OunceType : WeightType<Ounce>("oz", toBase(POUND_IN_OUNCE), fromBase(POUND_IN_OUNCE), ::Ounce)
}

/**
 * Abstract base class extended by specific Weight unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Weight : Measure, Amount, Operators<Weight>, Comparable<Weight> {

    /* Properties used to access this Weight's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val kilogram by lazy { KilogramType.fromBase(base) }
    val gram by lazy { GramType.fromBase(base) }
    val pound by lazy { PoundType.fromBase(base) }
    val ounce by lazy { OunceType.fromBase(base) }

    override fun equals(other: Any?) = other != null && other is Weight && base == other.base

    override fun hashCode() = Weight::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Weight): Int = base.compareTo(other.base)

    @JsName("convert")
    operator fun <T : Weight> invoke(w: WeightType<T>) = w.create(w.fromBase(base))
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
    override val type = KilogramType
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
    override val type = GramType
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
    override val type = PoundType
}

/**
 * Class that represents a [Weight] in ounces.
 *
 * @param value - Double value for a [Weight] in units of ounces.
 */
@Serializable
@SerialName("ounce")
@JsExport
class Ounce(override val value: Double = 0.0) : Weight(), MeasureOperators<Ounce, Weight> {
    override val type = OunceType
}

/* Constants used for converting from base unit(pounds) to other units */
const val POUND_IN_KILOGRAM = 0.453592
const val POUND_IN_GRAM = 453.592
const val POUND_IN_OUNCE = 16.0

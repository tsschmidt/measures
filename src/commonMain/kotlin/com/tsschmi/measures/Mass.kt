@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import com.tsschmi.measures.MassType.*

/**
 * Enum used to designate the unit of [Mass] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Mass] value in the common base units.
 * @param fromBase - Function that returns the [Mass] value in the configured units from the base unit.
 */
@JsExport
@Serializable
sealed class MassType<out T : Mass>(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double,
    override val create: (Double) -> T
): MeasureType<T> {
    @Serializable
    data object KilogramType : MassType<Kilogram>("kg", toBase(KILOGRAM_TO_POUND), fromBase(KILOGRAM_TO_POUND), ::Kilogram)

    @Serializable
    data object GramType : MassType<Gram>("g", toBase(GRAM_TO_POUND), fromBase(GRAM_TO_POUND), ::Gram)

    @Serializable
    data object PoundType : MassType<Pound>("lb", identity, identity, ::Pound)

    @Serializable
    data object OunceType : MassType<Ounce>("oz", toBase(OUNCE_TO_POUND), fromBase(OUNCE_TO_POUND), ::Ounce)


}

/**
 * Abstract base class extended by specific Mass unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Mass : Measure, Amount, Operators<Mass>, Comparable<Mass> {

    /* Properties used to access this Mass' value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val kilogram by lazy { KilogramType.fromBase(base) }
    val gram by lazy { GramType.fromBase(base) }
    val pound by lazy { PoundType.fromBase(base) }
    val ounce by lazy { OunceType.fromBase(base) }

    override fun equals(other: Any?) = other != null && other is Mass && base == other.base

    override fun hashCode() = Mass::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Mass): Int = base.compareTo(other.base)

    @JsExport.Ignore
    operator fun <T : Mass> invoke(w: MassType<T>) = w.create(w.fromBase(base))

    /** inc() and dec() implemented here instead of interface because of a Kotlin generics issue */
    @JsExport.Ignore
    operator fun inc() = this(value + 1.0)

    @JsExport.Ignore
    operator fun dec() = this(value - 1.0)
}

/**
 * Class that represents a [Mass] in kilograms.
 *
 * @param value - Double value for a [Mass] in units of Kilograms
 */
@Serializable
@SerialName("kilogram")
@JsExport
class Kilogram(override val value: Double = 0.0) : Mass(), MeasureOperators<Kilogram, Mass> {
    override val type = KilogramType
}
/**
 * Class that represents a [Mass] in Grams.
 *
 * @param value - Double value for a [Mass] in units of grams
 */

@Serializable
@SerialName("gram")
@JsExport
class Gram(override val value: Double = 0.0) : Mass(), MeasureOperators<Gram, Mass> {
    override val type = GramType
}

/**
 * Class that represents a [Mass] in pounds.
 *
 * @param value - Double value for a [Mass] in units of pounds.
 */
@Serializable
@SerialName("pound")
@JsExport
class Pound(override val value: Double = 0.0) : Mass(), MeasureOperators<Pound, Mass> {
    override val type = PoundType
}

/**
 * Class that represents a [Mass] in ounces.
 *
 * @param value - Double value for a [Mass] in units of ounces.
 */
@Serializable
@SerialName("ounce")
@JsExport
class Ounce(override val value: Double = 0.0) : Mass(), MeasureOperators<Ounce, Mass> {
    override val type = OunceType
}

/* Constants used for converting from base unit(pounds) to other units */
//const val POUND_IN_KILOGRAM = 0.453592
//const val POUND_IN_GRAM = 453.592
//const val POUND_IN_OUNCE = 16.0

const val KILOGRAM_TO_POUND = 2.20462
const val GRAM_TO_POUND = 0.00220462
const val OUNCE_TO_POUND = 0.0625

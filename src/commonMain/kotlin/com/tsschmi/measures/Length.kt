@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import com.tsschmi.measures.LengthType.*
import kotlin.js.JsName

/**
 * Enum implementing [MeasureType] to provide units of [Length] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Length] value in the common base units.
 * @param fromBase - Function that returns the [Length] value in the configured units from the base unit.
 */
@JsExport
@Serializable
sealed class LengthType<out T : Length>(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double,
    override val create: (Double) -> T
) : MeasureType<T>{
    @Serializable
    data object InchType : LengthType<Inch>("in", toBase(M_INCH), fromBase(M_INCH), ::Inch)

    @Serializable
    data object CentimeterType : LengthType<Centimeter>("cm", toBase(M_CM), fromBase(M_CM), ::Centimeter)

    @Serializable
    data object MeterType : LengthType<Meter>("m", identity, identity, ::Meter)

    @Serializable
    data object FootType : LengthType<Foot>("ft", toBase(M_FOOT), fromBase(M_FOOT), ::Foot)

    @Serializable
    data object KilometerType : LengthType<Kilometer>("km", toBase(M_KM), fromBase(M_KM), ::Kilometer)

    @Serializable
    data object MileType : LengthType<Mile>("mil", toBase(M_MILE), fromBase(M_MILE), ::Mile)

    @Serializable
    data object YardType : LengthType<Yard>("yd", toBase(M_YARD), fromBase(M_YARD), ::Yard)
}

/**
 * Abstract base class extended by specific Length unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Length : Measure, Operators<Length>, Comparable<Length> {
    /* Properties used to access this Gravity's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val inches by lazy { InchType.fromBase(base) }
    val cm by lazy { CentimeterType.fromBase(base) }
    val meter by lazy { MeterType.fromBase(base) }
    val feet by lazy { FootType.fromBase(base) }
    val kilometer by lazy { KilometerType.fromBase(base) }
    val mile by lazy { MileType.fromBase(base) }
    val yard by lazy { YardType.fromBase(base) }

    override fun equals(other: Any?) = other != null && other is Length && base == other.base

    override fun hashCode(): Int = Length::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Length): Int = base.compareTo(other.base)

    @JsName("Create")
    operator fun <T : Length> invoke(l : LengthType<T>) = l.create(l.fromBase(base))
}

/**
 * Class representing [Length] in inches.
 */
@Serializable
@SerialName("inch")
@JsExport
class Inch(override val value: Double = 0.0) : Length(), MeasureOperators<Inch, Length> {
    override val type = InchType

}

/**
 * Class representing [Length] in centimeters.
 */
@Serializable
@SerialName("centimeter")
@JsExport
class Centimeter(override val value: Double = 0.0) : Length(), MeasureOperators<Centimeter, Length> {
    override val type = CentimeterType
}

/**
 * Class representing [Length] in meters.
 */
@Serializable
@SerialName("meter")
@JsExport
class Meter(override val value: Double = 0.0) : Length(), MeasureOperators<Meter, Length> {
    override val type = MeterType
}

/**
 * Class representing [Length] in feet.
 */
@Serializable
@SerialName("inch")
@JsExport
class Foot(override val value: Double = 0.0) : Length(), MeasureOperators<Foot, Length> {
    override val type = FootType
}

/**
 * Class representing [Length] in kilometers.
 */
@Serializable
@SerialName("kilometer")
@JsExport
class Kilometer(override val value: Double = 0.0) : Length(), MeasureOperators<Kilometer, Length> {
    override val type = KilometerType
}

/**
 * Class representing [Length] in miles.
 */
@Serializable
@SerialName("mile")
@JsExport
class Mile(override val value: Double = 0.0) : Length(), MeasureOperators<Mile, Length> {
    override val type = MileType
}

/**
 * Class representing [Length] in yards.
 */
@Serializable
@SerialName("yard")
@JsExport
class Yard(override val value: Double = 0.0) : Length(), MeasureOperators<Yard, Length> {
    override val type = YardType
}

/** Constants used to convert [Length] values to base units */
const val M_INCH = 39.3701
const val M_FOOT = 3.28084
const val M_CM = 100.0
const val M_KM = 0.001
const val M_MILE = 0.000621371
const val M_YARD = 1.09361

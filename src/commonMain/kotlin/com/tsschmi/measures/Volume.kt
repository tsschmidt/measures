@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import com.tsschmi.measures.VolumeType.*
import kotlinx.serialization.Polymorphic
import kotlin.js.JsName

@JsExport
@Serializable
sealed class VolumeType<out T : Volume>(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double,
    override val create: (Double) -> T
) : MeasureType<T> {
    @Serializable
    data object LiterType : VolumeType<Liter>("l", identity, identity, ::Liter)

    @Serializable
    data object MilliliterType : VolumeType<Milliliter>("ml", toBase(MILLILITER_TO_LITER), fromBase(MILLILITER_TO_LITER), ::Milliliter)

    @Serializable
    data object GallonType : VolumeType<Gallon>("gal", toBase(GALLON_TO_LITER), fromBase(GALLON_TO_LITER), ::Gallon)

    @Serializable
    data object QuartType : VolumeType<Quart>("qt", toBase(QUART_TO_LITER), fromBase(QUART_TO_LITER), ::Quart)

    @Serializable
    data object PintType : VolumeType<Pint>("pt", toBase(PINT_TO_LITER), fromBase(PINT_TO_LITER), ::Pint)

    @Serializable
    data object CupType : VolumeType<Cup>("c", toBase(CUP_TO_LITER), fromBase(CUP_TO_LITER), ::Cup)

    @Serializable
    data object TablespoonType : VolumeType<Tablespoon>("tbsp", toBase(TABLESPOON_TO_LITER), fromBase(TABLESPOON_TO_LITER), ::Tablespoon)

    @Serializable
    data object TeaspoonType: VolumeType<Teaspoon>("tsp", toBase(TEASPOON_TO_LITER), fromBase(TEASPOON_TO_LITER), ::Teaspoon)

    @Serializable
    data object FluidOunceType: VolumeType<FluidOunce>("fl. oz", toBase(FLUID_OUNCE_TO_LITER), fromBase(FLUID_OUNCE_TO_LITER), ::FluidOunce)
}

/**
 * Abstract base class extended by specific Volume unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Volume : Measure, Amount, Operators<Volume>, Comparable<Volume> {
    /** Properties used to access this Volume's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val liter by lazy { LiterType.fromBase(base) }
    val milliliter by lazy { MilliliterType.fromBase(base) }
    val gallon by lazy { GallonType.fromBase(base) }
    val quart by lazy { QuartType.fromBase(base) }
    val pint by lazy { PintType.fromBase(base) }
    val cup by lazy { CupType.fromBase(base) }
    val tablespoon by lazy { TablespoonType.fromBase(base) }
    val teaspoon by lazy { TeaspoonType.fromBase(base) }
    val fluidOunce by lazy { FluidOunceType.fromBase(base) }

    override fun equals(other: Any?) = other != null && other is Volume && base == other.base

    override fun hashCode() = Volume::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Volume): Int = base.compareTo(other.base)

    @JsName("convert")
    operator fun <T : Volume> invoke(v: VolumeType<T>) = v.create(v.fromBase(base))

    /** inc() and dec() implemented here instead of interface because of a Kotlin generics issue */
    @JsExport.Ignore
    operator fun inc() = this(value + 1.0)

    @JsExport.Ignore
    operator fun dec() = this(value - 1.0)
}

/**
 * Class that represents [Volume] in liters.
 */
@Serializable
@SerialName("liter")
@JsExport
class Liter(override val value: Double = 0.0) : Volume(), MeasureOperators<Liter, Volume> {
    override val type = LiterType
}

/**
 * Class that represents [Volume] in milliliters.
 */
@Serializable
@SerialName("ml")
@JsExport
class Milliliter(override val value: Double = 0.0) : Volume(), MeasureOperators<Milliliter, Volume> {
    override val type = MilliliterType
}

/**
 * Class that represents [Volume] in gallons.
 */
@Serializable
@SerialName("gallon")
@JsExport
@Polymorphic
class Gallon(override val value: Double = 0.0) : Volume(), MeasureOperators<Gallon, Volume> {
    override val type = GallonType
}

/**
 * Class that represents [Volume] in quarts.
 */
@Serializable
@SerialName("quart")
@JsExport
class Quart(override val value: Double = 0.0) : Volume(), MeasureOperators<Quart, Volume> {
    override val type = QuartType
}

/**
 * Class that represents [Volume] in pints.
 */
@Serializable
@SerialName("pint")
@JsExport
class Pint(override val value: Double = 0.0) : Volume(), MeasureOperators<Pint, Volume> {
    override val type = PintType
}

/**
 * Class that represents [Volume] in cups.
 */
@Serializable
@SerialName("cup")
@JsExport
class Cup(override val value: Double = 0.0) : Volume(), MeasureOperators<Cup, Volume> {
    override val type = CupType
}

/**
 * Class that represents [Volume] in tablespoons.
 */
@Serializable
@SerialName("tablespoon")
@JsExport
class Tablespoon(override val value: Double = 0.0) : Volume(), MeasureOperators<Tablespoon, Volume> {
    override val type = TablespoonType
}

/**
 * Class that represents [Volume] in teaspoons.
 */
@Serializable
@SerialName("teaspoon")
@JsExport
class Teaspoon(override val value: Double = 0.0) : Volume(), MeasureOperators<Teaspoon, Volume> {
    override val type = TeaspoonType
}

/**
 * Class that represents [Volume] in fluid ounces.
 */
@Serializable
@SerialName("fl_ounce")
@JsExport
class FluidOunce(override val value: Double = 0.0) : Volume(), MeasureOperators<FluidOunce, Volume> {
    override val type = FluidOunceType
}

/** Constants used in converting [Volume] values to and from base units */
//const val L_GALLON = 0.264172
//const val L_QUART = 1.05669
//const val L_MILLILITER = 1000.0
//const val L_PINT = 2.11338
//const val L_CUP = 4.16667
//const val L_TABLESPOON = 67.628
//const val L_TEASPOON = 202.884
//const val L_FLUID_OUNCE = 33.814

const val GALLON_TO_LITER = 3.78541
const val QUART_TO_LITER = 0.946353
const val MILLILITER_TO_LITER = 0.001
const val PINT_TO_LITER = 0.473176
const val CUP_TO_LITER = 0.24
const val TABLESPOON_TO_LITER = 0.0147868
const val TEASPOON_TO_LITER = 0.00492892
const val FLUID_OUNCE_TO_LITER = 0.0295735



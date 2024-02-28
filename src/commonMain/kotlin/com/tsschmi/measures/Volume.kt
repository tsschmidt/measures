@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import com.tsschmi.measures.VolumeType.*

@JsExport
@Serializable
sealed class VolumeType(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double
) : MeasureType {
    @Serializable
    data object LiterType : VolumeType("l", lToQt, qtToL)

    @Serializable
    data object MilliliterType : VolumeType("ml", mlToQt, qtToMl)

    @Serializable
    data object GallonType : VolumeType("gal", galToQt, qtToGal)

    @Serializable
    data object QuartType : VolumeType("qt", identity, identity)

    @Serializable
    data object PintType : VolumeType("pt", pToQt, qtToP)

    @Serializable
    data object CupType : VolumeType("c", cToQt, qtToC)

    @Serializable
    data object TablespoonType : VolumeType("tbsp", tbspToQt, qtToTbsp)

    @Serializable
    data object TeaspoonType: VolumeType("tsp", tspToQt, qtToTsp)

    @Serializable
    data object FluidOunceType: VolumeType("fl. oz", flozToQt, qtToFloz)
}

/**
 * Abstract base class extended by specific Volume unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Volume : Measure, Amount, Comparable<Volume> {
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
}

/**
 * Class that represents [Volume] in liters.
 */
@Serializable
@SerialName("liter")
@JsExport
class Liter(override val value: Double = 0.0) : Volume(), MeasureOperators<Liter, Volume> {
    override val type: MeasureType = LiterType
    override fun create(v: Double) = Liter(v)
}

/**
 * Class that represents [Volume] in milliliters.
 */
@Serializable
@SerialName("ml")
@JsExport
class Milliliter(override val value: Double = 0.0) : Volume(), MeasureOperators<Milliliter, Volume> {
    override val type: MeasureType = MilliliterType
    override fun create(v: Double) = Milliliter(v)

}

/**
 * Class that represents [Volume] in gallons.
 */
@Serializable
@SerialName("gallon")
@JsExport
class Gallon(override val value: Double = 0.0) : Volume(), MeasureOperators<Gallon, Volume> {
    override val type: MeasureType = GallonType
    override fun create(v: Double) = Gallon(v)
}

/**
 * Class that represents [Volume] in quarts.
 */
@Serializable
@SerialName("quart")
@JsExport
class Quart(override val value: Double = 0.0) : Volume(), MeasureOperators<Quart, Volume> {
    override val type: MeasureType = QuartType
    override fun create(v: Double) = Quart(v)
}

/**
 * Class that represents [Volume] in pints.
 */
@Serializable
@SerialName("pint")
@JsExport
class Pint(override val value: Double = 0.0) : Volume(), MeasureOperators<Pint, Volume> {
    override val type: MeasureType = PintType
    override fun create(v: Double) = Pint(v)
}

/**
 * Class that represents [Volume] in cups.
 */
@Serializable
@SerialName("cup")
@JsExport
class Cup(override val value: Double = 0.0) : Volume(), MeasureOperators<Cup, Volume> {
    override val type: MeasureType = CupType
    override fun create(v: Double) = Cup(v)
}

/**
 * Class that represents [Volume] in tablespoons.
 */
@Serializable
@SerialName("tablespoon")
@JsExport
class Tablespoon(override val value: Double = 0.0) : Volume(), MeasureOperators<Tablespoon, Volume> {
    override val type: MeasureType = TablespoonType
    override fun create(v: Double) = Tablespoon(v)
}

/**
 * Class that represents [Volume] in teaspoons.
 */
@Serializable
@SerialName("teaspoon")
@JsExport
class Teaspoon(override val value: Double = 0.0) : Volume(), MeasureOperators<Teaspoon, Volume> {
    override val type: MeasureType = TeaspoonType
    override fun create(v: Double) = Teaspoon(v)
}

/**
 * Class that represents [Volume] in fluid ounces.
 */
@Serializable
@SerialName("fl_ounce")
@JsExport
class FluidOunce(override val value: Double = 0.0) : Volume(), MeasureOperators<FluidOunce, Volume> {
    override val type: MeasureType = FluidOunceType
    override fun create(v: Double) = FluidOunce(v)
}

/** Constants used in converting [Volume] values to and from base units */
const val QT_GALLON = 4.0
const val QT_LITER = 1.05669
const val QT_MILLILITER = 0.00105669
const val QT_PINT = 0.5
const val QT_CUP = 0.25
const val QT_TABLESPOON = 0.015625
const val QT_TEASPOON = 0.00520833
const val QT_FLUID_OUNCE = 0.03215

/** Functions to convert [Volume] values from a base unit to other units */
val galToQt = { v: Double -> v * QT_GALLON }
val qtToGal = { v: Double -> v / QT_GALLON }
val lToQt = { v: Double -> v * QT_LITER }
val qtToL = { v: Double -> v / QT_LITER }
val mlToQt = { v: Double -> v * QT_MILLILITER }
val qtToMl = { v: Double -> v / QT_MILLILITER }
val pToQt = { v: Double -> v * QT_PINT }
val qtToP = { v: Double -> v / QT_PINT }
val cToQt = { v: Double -> v * QT_CUP }
val qtToC = { v: Double -> v / QT_CUP }
val tbspToQt = { v: Double -> v * QT_TABLESPOON }
val qtToTbsp = { v: Double -> v / QT_TABLESPOON }
val tspToQt = { v: Double -> v * QT_TEASPOON }
val qtToTsp = { v: Double -> v / QT_TEASPOON }
val flozToQt = { v: Double -> v * QT_FLUID_OUNCE }
val qtToFloz = { v: Double -> v / QT_FLUID_OUNCE }

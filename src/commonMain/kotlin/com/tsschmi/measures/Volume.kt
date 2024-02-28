@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import com.tsschmi.measures.VolumeType.*
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
    data object LiterType : VolumeType<Liter>("l", lToQt, qtToL, ::Liter)

    @Serializable
    data object MilliliterType : VolumeType<Milliliter>("ml", mlToQt, qtToMl, ::Milliliter)

    @Serializable
    data object GallonType : VolumeType<Gallon>("gal", galToQt, qtToGal, ::Gallon)

    @Serializable
    data object QuartType : VolumeType<Quart>("qt", identity, identity, ::Quart)

    @Serializable
    data object PintType : VolumeType<Pint>("pt", pToQt, qtToP, ::Pint)

    @Serializable
    data object CupType : VolumeType<Cup>("c", cToQt, qtToC, ::Cup)

    @Serializable
    data object TablespoonType : VolumeType<Tablespoon>("tbsp", tbspToQt, qtToTbsp, ::Tablespoon)

    @Serializable
    data object TeaspoonType: VolumeType<Teaspoon>("tsp", tspToQt, qtToTsp, ::Teaspoon)

    @Serializable
    data object FluidOunceType: VolumeType<FluidOunce>("fl. oz", flozToQt, qtToFloz, ::FluidOunce)
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

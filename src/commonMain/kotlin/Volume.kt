@file:OptIn(ExperimentalJsExport::class)

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import VolumeType.*

@JsExport
@Serializable
sealed class VolumeType(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double
) : MeasureType<Volume> {

    @Serializable
    object LITER : VolumeType("l", lToQt, qtToL){
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Liter(v) as T
    }

    @Serializable
    object MILLILITER : VolumeType("ml", mlToQt, qtToMl){
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Milliliter(v) as T
    }

    @Serializable
    object GALLON : VolumeType("gal", galToQt, qtToGal) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Liter(v) as T
    }

    @Serializable
    object QUART : VolumeType("qt", identity, identity) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Quart(v) as T
    }

    @Serializable
    object PINT : VolumeType("pt", pToQt, qtToP) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Pint(v) as T
    }

    @Serializable
    object CUP : VolumeType("c", cToQt, qtToC) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Cup(v) as T
    }

    @Serializable
    object TABLESPOON : VolumeType("tbsp", tbspToQt, qtToTbsp) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Tablespoon(v) as T
    }

    @Serializable
    object TEASPOON: VolumeType("tsp", tspToQt, qtToTsp){
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Teaspoon(v) as T
    }

    @Serializable
    object FLUID_OUNCE: VolumeType("fl. oz", flozToQt, qtToFloz){
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = FluidOunce(v) as T
    }
}

/**
 * Abstract base class extended by specific Volume unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Volume : Amount(), Comparable<Volume> {
    /** Properties used to access this Volume's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val liter by lazy { LITER.fromBase(base) }
    val milliliter by lazy { MILLILITER.fromBase(base) }
    val gallon by lazy { GALLON.fromBase(base) }
    val quart by lazy { QUART.fromBase(base) }
    val pint by lazy { PINT.fromBase(base) }
    val cup by lazy { CUP.fromBase(base) }
    val tablespoon by lazy { TABLESPOON.fromBase(base) }
    val teaspoon by lazy { TEASPOON.fromBase(base) }
    val fluidOunce by lazy { FLUID_OUNCE.fromBase(base) }

    override fun equals(other: Any?) = other != null && other is Volume && base == other.base

    override fun hashCode() = Volume::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Volume): Int = base.compareTo(other.base)

    operator fun <T : Volume> plus(o: Volume) : T = type.create(type.fromBase(base + o.base))

    operator fun <T : Volume> minus(o: Volume): T = type.create(type.fromBase(base - o.base))

    operator fun <T : Volume> times(o: Volume): T = type.create(type.fromBase(base * o.base))

    operator fun <T : Volume> div(o: Volume): T = type.create(type.fromBase(base / o.base))

    operator fun <T : Volume> rem(o: Volume): T = type.create(type.fromBase(base % o.base))

    operator fun <T : Volume> unaryPlus(): T = type.create(-value)

    operator fun <T : Volume> unaryMinus(): T = type.create(+value)

    operator fun <T : Volume> inc(): T = type.create(value + 1.0)

    operator fun <T : Volume> dec(): T = type.create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : Volume> plus(v: Double): T = type.create(value + v)

    @JsName("minusDouble")
    operator fun <T : Volume> minus(v: Double): T = type.create(value - v)

    @JsName("timesDouble")
    operator fun <T : Volume> times(v: Double): T = type.create(value * v)

    @JsName("divDouble")
    operator fun <T : Volume> div(v: Double): T = type.create(value / v)

    @JsName("remDouble")
    operator fun <T : Volume> rem(v: Double): T = type.create(value % v)

    @JsName("convert")
    operator fun <T : Volume> invoke(type: VolumeType): T = type.create(type.fromBase(base))
}

/**
 * Class that represents [Volume] in liters.
 */
@Serializable
@SerialName("liter")
@JsExport
class Liter(override val value: Double = 0.0) : Volume() {
    override val type = LITER
}

/**
 * Class that represents [Volume] in milliliters.
 */
@Serializable
@SerialName("ml")
@JsExport
class Milliliter(override val value: Double = 0.0) : Volume() {
    override val type = MILLILITER
}

/**
 * Class that represents [Volume] in gallons.
 */
@Serializable
@SerialName("gallon")
@JsExport
class Gallon(override val value: Double = 0.0) : Volume() {
    override val type = GALLON
}

/**
 * Class that represents [Volume] in quarts.
 */
@Serializable
@SerialName("quart")
@JsExport
class Quart(override val value: Double = 0.0) : Volume() {
    override val type = QUART
}

/**
 * Class that represents [Volume] in pints.
 */
@Serializable
@SerialName("pint")
@JsExport
class Pint(override val value: Double = 0.0) : Volume() {
    override val type = PINT
}

/**
 * Class that represents [Volume] in cups.
 */
@Serializable
@SerialName("cup")
@JsExport
class Cup(override val value: Double = 0.0) : Volume() {
    override val type = CUP
}

/**
 * Class that represents [Volume] in tablespoons.
 */
@Serializable
@SerialName("tablespoon")
@JsExport
class Tablespoon(override val value: Double = 0.0) : Volume() {
    override val type = TABLESPOON
}

/**
 * Class that represents [Volume] in teaspoons.
 */
@Serializable
@SerialName("teaspoon")
@JsExport
class Teaspoon(override val value: Double = 0.0) : Volume() {
    override val type = TEASPOON
}

/**
 * Class that represents [Volume] in fluid ounces.
 */
@Serializable
@SerialName("fl_ounce")
@JsExport
class FluidOunce(override val value: Double = 0.0) : Volume() {
    override val type = FLUID_OUNCE
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

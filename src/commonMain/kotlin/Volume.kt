@file:OptIn(ExperimentalJsExport::class)

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

@JsExport
enum class VolumeType(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double
) : MeasureType {
    LITER("l", lToQt, qtToL),
    MILLILITER("ml", mlToQt, qtToMl),
    GALLON("gal", galToQt, qtToGal),
    QUART("qt", identity, identity),
    PINT("pt", pToQt, qtToP),
    CUP("c", cToQt, qtToC),
    TABLESPOON("tbsp", tbspToQt, qtToTbsp),
    TEASPOON("tsp", tspToQt, qtToTsp),
    FLUID_OUNCE("fl. oz", flozToQt, qtToFloz)
}

/**
 * Abstract base class extended by specific Volume unit classes.
 */
@Serializable
@JsExport
sealed class Volume : Amount(), Comparable<Volume> {
    /** Properties used to access this Volume's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val liter by lazy { VolumeType.LITER.fromBase(base) }
    val milliliter by lazy { VolumeType.MILLILITER.fromBase(base) }
    val gallon by lazy { VolumeType.GALLON.fromBase(base) }
    val quart by lazy { VolumeType.QUART.fromBase(base) }
    val pint by lazy { VolumeType.PINT.fromBase(base) }
    val cup by lazy { VolumeType.CUP.fromBase(base) }
    val tablespoon by lazy { VolumeType.TABLESPOON.fromBase(base) }
    val teaspoon by lazy { VolumeType.TEASPOON.fromBase(base) }
    val fluidOunce by lazy { VolumeType.FLUID_OUNCE.fromBase(base) }

    /** Returns the Volume's value in liters in a new [Liter] instance. */
    @Suppress("UNUSED")
    fun toLiters() = Liter(liter)

    /** Returns the Volume's value in milliliters in a new [Milliliter] instance. */
    @Suppress("UNUSED")
    fun toMilliliters() = Milliliter(milliliter)

    /** Returns the Volume's value in gallons in a new [Gallon] instance. */
    @Suppress("UNUSED")
    fun toGallons() = Gallon(gallon)

    /** Returns the Volume's value in quarts in a new [Quart] instance. */
    @Suppress("UNUSED")
    fun toQuarts() = Quart(quart)

    /** Returns the Volume's value in pints in a new [Pint] instance. */
    @Suppress("UNUSED")
    fun toPints() = Pint(pint)

    /** Returns the Volume's value in cups in a new [Cup] instance. */
    @Suppress("UNUSED")
    fun toCups() = Cup(cup)

    /** Returns the Volume's value in tablespoons in a new [Tablespoon] instance. */
    @Suppress("UNUSED")
    fun toTablespoons() = Tablespoon(tablespoon)

    /** Returns the Volume's value in teaspoons in a new [Teaspoon] instance. */
    @Suppress("UNUSED")
    fun toTeaspoons() = Teaspoon(teaspoon)

    /** Returns the Volume's value in FluidOunce in a new [FluidOunce] instance. */
    @Suppress("UNUSED")
    fun toFluidOunces() = FluidOunce(fluidOunce)

    override fun equals(other: Any?) = other != null && other is Volume && base == other.base

    override fun hashCode() = Volume::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Volume): Int = base.compareTo(other.base)

    operator fun <T : Volume> plus(o: Volume) : T = create(type.fromBase(base + o.base))

    operator fun <T : Volume> minus(o: Volume): T = create(type.fromBase(base - o.base))

    operator fun <T : Volume> times(o: Volume): T = create(type.fromBase(base * o.base))

    operator fun <T : Volume> div(o: Volume): T = create(type.fromBase(base / o.base))

    operator fun <T : Volume> rem(o: Volume): T = create(type.fromBase(base % o.base))

    operator fun <T : Volume> unaryPlus(): T = create(-value)

    operator fun <T : Volume> unaryMinus(): T = create(+value)

    operator fun <T : Volume> inc(): T = create(value + 1.0)

    operator fun <T : Volume> dec(): T = create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : Volume> plus(v: Double): T = create(value + v)

    @JsName("minusDouble")
    operator fun <T : Volume> minus(v: Double): T = create(value - v)

    @JsName("timesDouble")
    operator fun <T : Volume> times(v: Double): T = create(value * v)

    @JsName("divDouble")
    operator fun <T : Volume> div(v: Double): T = create(value / v)

    @JsName("remDouble")
    operator fun <T : Volume> rem(v: Double): T = create(value % v)
}

/**
 * Class that represents [Volume] in liters.
 */
@Serializable
@SerialName("liter")
@JsExport
class Liter(override val value: Double = 0.0) : Volume() {
    override val type: MeasureType = VolumeType.LITER

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Liter(v) as T
}

/**
 * Class that represents [Volume] in milliliters.
 */
@Serializable
@SerialName("ml")
@JsExport
class Milliliter(override val value: Double = 0.0) : Volume() {
    override val type: MeasureType = VolumeType.MILLILITER

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Milliliter(v) as T
}

/**
 * Class that represents [Volume] in gallons.
 */
@Serializable
@SerialName("gallon")
@JsExport
class Gallon(override val value: Double = 0.0) : Volume() {
    override val type: MeasureType = VolumeType.GALLON

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Gallon(v) as T
}

/**
 * Class that represents [Volume] in quarts.
 */
@Serializable
@SerialName("quart")
@JsExport
class Quart(override val value: Double = 0.0) : Volume() {
    override val type: MeasureType = VolumeType.QUART

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Quart(v) as T
}

/**
 * Class that represents [Volume] in pints.
 */
@Serializable
@SerialName("pint")
@JsExport
class Pint(override val value: Double = 0.0) : Volume() {
    override val type: MeasureType = VolumeType.PINT

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Pint(v) as T
}

/**
 * Class that represents [Volume] in cups.
 */
@Serializable
@SerialName("cup")
@JsExport
class Cup(override val value: Double = 0.0) : Volume() {
    override val type: MeasureType = VolumeType.CUP

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Cup(v) as T
}

/**
 * Class that represents [Volume] in tablespoons.
 */
@Serializable
@SerialName("tablespoon")
@JsExport
class Tablespoon(override val value: Double = 0.0) : Volume() {
    override val type: MeasureType = VolumeType.TABLESPOON

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Tablespoon(v) as T
}

/**
 * Class that represents [Volume] in teaspoons.
 */
@Serializable
@SerialName("teaspoon")
@JsExport
class Teaspoon(override val value: Double = 0.0) : Volume() {
    override val type: MeasureType = VolumeType.TEASPOON

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Teaspoon(v) as T
}

/**
 * Class that represents [Volume] in fluid ounces.
 */
@Serializable
@SerialName("fl_ounce")
@JsExport
class FluidOunce(override val value: Double = 0.0) : Volume() {
    override val type: MeasureType = VolumeType.FLUID_OUNCE

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = FluidOunce(v) as T
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

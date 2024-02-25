@file:OptIn(ExperimentalJsExport::class)

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

/**
 * Enum used to designate the unit of [Weight] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Weight] value in the common base units.
 * @param fromBase - Function that returns the [Weight] value in the configured units from the base unit.
 */
@JsExport
enum class WeightType(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double
) : MeasureType {
    KILOGRAM("kg", kgToLb, lbToKg),
    GRAM("g", gToLb, lbToG),
    POUND("lb", identity, identity),
    OUNCE("oz", ozToLb, lbToOz)
}

/**
 * Abstract base class extended by specific Weight unit classes.
 */
@Serializable
@JsExport
sealed class Weight: Amount(), Comparable<Weight> {

    /* Properties used to access this Weight's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val kilogram by lazy { WeightType.KILOGRAM.fromBase(base) }
    val gram by lazy { WeightType.GRAM.fromBase(base) }
    val pound by lazy { WeightType.POUND.fromBase(base) }
    val ounce by lazy { WeightType.OUNCE.fromBase(base) }

    /** Returns the Weight's value in kilograms in a new [Kilogram] instance. */
    @Suppress("UNUSED")
    fun toKilograms() = Kilogram(kilogram)

    /** Returns the Weight's value in grams in a new [Gram] instance. */
    @Suppress("UNUSED")
    fun toGram() = Gram(gram)

    /** Returns the Weight's value in pounds in a new [Pound] instance. */
    @Suppress("UNUSED")
    fun toPound() = Pound(pound)

    /** Returns the Weight's value in ounces in a new [Ounce] instance. */
    @Suppress("UNUSED")
    fun toOunce() = Ounce(ounce)

    /**
     * Default toString to display value with 2 significant digits without units.
     */
    override fun toString() = format(2)

    override fun equals(other: Any?) = other != null && other is Weight && base == other.base

    override fun hashCode() =  Weight::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Weight): Int = base.compareTo(other.base)

    operator fun <T : Weight> plus(o: Weight) : T = create(type.fromBase(base + o.base))

    operator fun <T : Weight> minus(o: Weight): T = create(type.fromBase(base - o.base))

    operator fun <T : Weight> times(o: Weight): T = create(type.fromBase(base * o.base))

    operator fun <T : Weight> div(o: Weight): T = create(type.fromBase(base / o.base))

    operator fun <T : Weight> rem(o: Weight): T = create(type.fromBase(base % o.base))

    operator fun <T : Weight> unaryPlus(): T = create(-value)

    operator fun <T : Weight> unaryMinus(): T = create(+value)

    operator fun <T : Weight> inc(): T = create(value + 1.0)

    operator fun <T : Weight> dec(): T = create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : Weight> plus(v: Double): T = create(value + v)

    @JsName("minusDouble")
    operator fun <T : Weight> minus(v: Double): T = create(value - v)

    @JsName("timesDouble")
    operator fun <T : Weight> times(v: Double): T = create(value * v)

    @JsName("divDouble")
    operator fun <T : Weight> div(v: Double): T = create(value / v)

    @JsName("remDouble")
    operator fun <T : Weight> rem(v: Double): T = create(value % v)
}

/**
 * Class that represents a [Weight] in kilograms.
 *
 * @param value - Double value for a [Weight] in units of Kilograms
 */
@Serializable
@SerialName("kilogram")
@JsExport
class Kilogram(override val value: Double = 0.0) : Weight() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double) = Kilogram(v) as T

    override val type: MeasureType = WeightType.KILOGRAM
}

/**
 * Class that represents a [Weight] in Grams.
 *
 * @param value - Double value for a [Weight] in units of grams
 */
@Serializable
@SerialName("gram")
@JsExport
class Gram(override val value: Double = 0.0) : Weight() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double) = Gram(v) as T

    override val type: MeasureType = WeightType.GRAM
}

/**
 * Class that represents a [Weight] in pounds.
 *
 * @param value - Double value for a [Weight] in units of pounds.
 */
@Serializable
@SerialName("pound")
@JsExport
class Pound(override val value: Double = 0.0) : Weight() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double) = Pound(v) as T

    override val type: MeasureType = WeightType.POUND
}

/**
 * Class that represents a [Weight] in ounces.
 *
 * @param value - Double value for a [Weight] in units of ounces.
 */
@Serializable
@SerialName("ounce")
@JsExport
class Ounce(override val value: Double = 0.0): Weight() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double) = Ounce(v) as T

    override val type: MeasureType = WeightType.OUNCE
}

/* Constants used for converting from base unit(pounds) to other units */
const val POUND_KILOGRAM = 0.453592
const val POUND_GRAM = 0.00220462
const val POUND_OUNCE = 0.0625

/* Functions used for converting values between Weight units to the base unit(pounds) */
val lbToKg = { v: Double -> v * POUND_KILOGRAM }
val kgToLb = { v: Double -> v / POUND_KILOGRAM }
val lbToG = { v: Double -> v * POUND_GRAM }
val gToLb = { v: Double -> v / POUND_GRAM }
val ozToLb = { v: Double -> v * POUND_OUNCE }
val lbToOz = { v: Double -> v / POUND_OUNCE }
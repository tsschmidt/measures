@file:OptIn(ExperimentalJsExport::class)

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import WeightType.*

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
    override val fromBase: (Double) -> Double,
) : MeasureType<Weight> {
    KILOGRAM("kg", kgToLb, lbToKg) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double) = Kilogram(v) as T
    },
    GRAM("g", gToLb, lbToG) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double) = Gram(v) as T
    },
    POUND("lb", identity, identity) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double) = Pound(v) as T
    },
    OUNCE("oz", ozToLb, lbToOz){
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double) = Ounce(v) as T
    };
}

/**
 * Abstract base class extended by specific Weight unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Weight(override val type: MeasureType<Weight>): Amount(), Comparable<Weight> {

    /* Properties used to access this Weight's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val kilogram by lazy { KILOGRAM.fromBase(base) }
    val gram by lazy { GRAM.fromBase(base) }
    val pound by lazy { POUND.fromBase(base) }
    val ounce by lazy { OUNCE.fromBase(base) }

    /**
     * Default toString to display value with 2 significant digits without units.
     */
    override fun toString() = format(2)

    override fun equals(other: Any?) = other != null && other is Weight && base == other.base

    override fun hashCode() =  Weight::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Weight): Int = base.compareTo(other.base)

    operator fun <T : Weight> plus(o: Weight) : T = type.create(type.fromBase(base + o.base))

    operator fun <T : Weight> minus(o: Weight): T = type.create(type.fromBase(base - o.base))

    operator fun <T : Weight> times(o: Weight): T = type.create(type.fromBase(base * o.base))

    operator fun <T : Weight> div(o: Weight): T = type.create(type.fromBase(base / o.base))

    operator fun <T : Weight> rem(o: Weight): T = type.create(type.fromBase(base % o.base))

    operator fun <T : Weight> unaryPlus(): T = type.create(-value)

    operator fun <T : Weight> unaryMinus(): T = type.create(+value)

    operator fun <T : Weight> inc(): T = type.create(value + 1.0)

    operator fun <T : Weight> dec(): T = type.create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : Weight> plus(v: Double): T = type.create(value + v)

    @JsName("minusDouble")
    operator fun <T : Weight> minus(v: Double): T = type.create(value - v)

    @JsName("timesDouble")
    operator fun <T : Weight> times(v: Double): T = type.create(value * v)

    @JsName("divDouble")
    operator fun <T : Weight> div(v: Double): T = type.create(value / v)

    @JsName("remDouble")
    operator fun <T : Weight> rem(v: Double): T = type.create(value % v)

    @JsName("convert")
    operator fun <T : Weight> invoke(type: WeightType): T = type.create(type.fromBase(base))
}

/**
 * Class that represents a [Weight] in kilograms.
 *
 * @param value - Double value for a [Weight] in units of Kilograms
 */
@Serializable
@SerialName("kilogram")
@JsExport
class Kilogram(override val value: Double = 0.0) : Weight(KILOGRAM)

/**
 * Class that represents a [Weight] in Grams.
 *
 * @param value - Double value for a [Weight] in units of grams
 */
@Serializable
@SerialName("gram")
@JsExport
class Gram(override val value: Double = 0.0) : Weight(GRAM)

/**
 * Class that represents a [Weight] in pounds.
 *
 * @param value - Double value for a [Weight] in units of pounds.
 */
@Serializable
@SerialName("pound")
@JsExport
class Pound(override val value: Double = 0.0) : Weight(POUND)

/**
 * Class that represents a [Weight] in ounces.
 *
 * @param value - Double value for a [Weight] in units of ounces.
 */
@Serializable
@SerialName("ounce")
@JsExport
class Ounce(override val value: Double = 0.0): Weight(OUNCE)

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
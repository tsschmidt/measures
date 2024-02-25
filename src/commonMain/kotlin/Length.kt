@file:OptIn(ExperimentalJsExport::class)

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

/**
 * Enum implementing [MeasureType] to provide units of [Length] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Length] value in the common base units.
 * @param fromBase - Function that returns the [Length] value in the configured units from the base unit.
 */
@JsExport
enum class LengthType(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double
) : MeasureType {
    INCH("in", inToCm, cmToIn),
    CM("cm", identity, identity),
    METER("m", mToCm, cmToM),
    FOOT("ft", ftToCm, cmToFt),
    KILOMETER("km", kmToCm, cmToKm),
    MILE("mil", milToCm, cmToMil),
    YARD("yd", ydToCm, cmToYd)
}

/**
 * Abstract base class extended by specific Length unit classes.
 */
@Serializable
@JsExport
sealed class Length : BaseMeasure(), Comparable<Length> {
    /* Properties used to access this Gravity's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val inches by lazy { LengthType.INCH.fromBase(base) }
    val cm by lazy { LengthType.CM.fromBase(base) }
    val meter by lazy { LengthType.METER.fromBase(base) }
    val feet by lazy { LengthType.FOOT.fromBase(base) }
    val kilometer by lazy { LengthType.KILOMETER.fromBase(base) }
    val mile by lazy { LengthType.MILE.fromBase(base) }
    val yard by lazy { LengthType.YARD.fromBase(base) }

    @Suppress("UNUSED")
    /** Returns the Length's value in inches in a new [Inch] instance. */
    fun toInches() = Inch(inches)

    @Suppress("UNUSED")
            /** Returns the Length's value in centimeters in a new [Centimeter] instance. */
    fun toCM() = Centimeter(cm)

    @Suppress("UNUSED")
            /** Returns the Length's value in meters in a new [Meter] instance. */
    fun toMeter() = Meter(meter)

    @Suppress("UNUSED")
            /** Returns the Length's value in feet in a new [Foot] instance. */
    fun toFeet() = Foot(feet)

    @Suppress("UNUSED")
            /** Returns the Length's value in yards in a new [Yard] instance. */
    fun toYard() = Yard(yard)

    @Suppress("UNUSED")
            /** Returns the Length's value in kilometers in a new [Kilometer] instance. */
    fun toKilometer() = Kilometer(kilometer)

    @Suppress("UNUSED")
            /** Returns the Length's value in miles in a new [Mile] instance. */
    fun toMile() = Mile(mile)

    override fun equals(other: Any?) = other != null && other is Length && base == other.base

    override fun hashCode(): Int = Length::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Length): Int = base.compareTo(other.base)

    operator fun <T : Length> plus(o: Length) : T = create(type.fromBase(base + o.base))

    operator fun <T : Length> minus(o: Length): T = create(type.fromBase(base - o.base))

    operator fun <T : Length> times(o: Length): T = create(type.fromBase(base * o.base))

    operator fun <T : Length> div(o: Length): T = create(type.fromBase(base / o.base))

    operator fun <T : Length> rem(o: Length): T = create(type.fromBase(base % o.base))

    operator fun <T : Length> unaryPlus(): T = create(-value)

    operator fun <T : Length> unaryMinus(): T = create(+value)

    operator fun <T : Length> inc(): T = create(value + 1.0)

    operator fun <T : Length> dec(): T = create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : Length> plus(v: Double): T = create(value + v)

    @JsName("minusDouble")
    operator fun <T : Length> minus(v: Double): T = create(value - v)

    @JsName("timesDouble")
    operator fun <T : Length> times(v: Double): T = create(value * v)

    @JsName("divDouble")
    operator fun <T : Length> div(v: Double): T = create(value / v)

    @JsName("remDouble")
    operator fun <T : Length> rem(v: Double): T = create(value % v)
}

/**
 * Class representing [Length] in inches.
 */
@Serializable
@SerialName("inch")
@JsExport
class Inch(override val value: Double = 0.0) : Length() {
    override val type: MeasureType = LengthType.INCH

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Inch(v) as T
}

/**
 * Class representing [Length] in centimeters.
 */
@Serializable
@SerialName("centimeter")
@JsExport
class Centimeter(override val value: Double = 0.0) : Length() {
    override val type: MeasureType = LengthType.CM

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Centimeter(v) as T
}

/**
 * Class representing [Length] in meters.
 */
@Serializable
@SerialName("meter")
@JsExport
class Meter(override val value: Double = 0.0) : Length() {
    override val type: MeasureType = LengthType.METER

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Meter(v) as T
}

/**
 * Class representing [Length] in feet.
 */
@Serializable
@SerialName("inch")
@JsExport
class Foot(override val value: Double = 0.0) : Length() {
    override val type: MeasureType = LengthType.FOOT

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Foot(v) as T
}

/**
 * Class representing [Length] in kilometers.
 */
@Serializable
@SerialName("kilometer")
@JsExport
class Kilometer(override val value: Double = 0.0) : Length() {
    override val type: MeasureType = LengthType.KILOMETER

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Kilometer(v) as T
}

/**
 * Class representing [Length] in miles.
 */
@Serializable
@SerialName("mile")
@JsExport
class Mile(override val value: Double = 0.0) : Length() {
    override val type: MeasureType = LengthType.MILE

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Mile(v) as T
}

/**
 * Class representing [Length] in yards.
 */
@Serializable
@SerialName("yard")
@JsExport
class Yard(override val value: Double = 0.0) : Length() {
    override val type: MeasureType = LengthType.YARD

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T  = Yard(v) as T
}

/** Constants used to convert [Length] values to base units */
const val CM_INCH = 2.54
const val CM_FOOT = 30.48
const val CM_METER = 100.0
const val CM_KM = CM_METER * 1000.0
const val CM_MILE = 160934.0
const val CM_YARD = CM_FOOT * 3.0

/** Functions for converting [Length] values to base units and back to other units. */
val inToCm = { v: Double -> v * CM_INCH }
val cmToIn = { v: Double -> v / CM_INCH }
val ftToCm = { v: Double -> v * CM_FOOT }
val cmToFt = { v: Double -> v / CM_FOOT }
val mToCm = { v: Double -> v * CM_METER }
val cmToM = { v: Double -> v / CM_METER }
val kmToCm = { v: Double -> v * CM_KM }
val cmToKm = { v: Double -> v / CM_KM }
val milToCm = { v: Double -> v * CM_MILE }
val cmToMil = { v: Double -> v / CM_MILE }
val ydToCm = { v: Double -> v * CM_YARD }
val cmToYd = { v: Double -> v / CM_YARD }

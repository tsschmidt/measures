@file:OptIn(ExperimentalJsExport::class)

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

/**
 * Enum implementing [MeasureType] to provide units of [Time] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Time] value in the common base units.
 * @param fromBase - Function that returns the [Time] value in the configured units from the base unit.
 */
@JsExport
enum class TimeType(
    override val units: String,
    override val toBase : (Double) -> Double,
    override val fromBase : (Double) -> Double
) : MeasureType {
    SECONDS("sec", identity, identity),
    MINUTES("min", mToS, sToM),
    HOURS("hrs", hToS, sToH),
    DAYS("dys", dayToS, sToDay),
    WEEKS("wks", wkToS, sToWk),
    MONTHS("mos", mthToS, sToMth),
    YEARS("yrs", yrToS, sToYr)
}

/**
 * Abstract base class extended by specific Time unit classes.
 */
@Serializable
@JsExport
sealed class Time : BaseMeasure(), Comparable<Time> {
    /* Properties used to access this Time's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val seconds by lazy { TimeType.SECONDS.fromBase(base) }
    val minutes by lazy { TimeType.MINUTES.fromBase(base) }
    val hours by lazy { TimeType.HOURS.fromBase(base) }
    val days by lazy { TimeType.DAYS.fromBase(base) }
    val weeks by lazy { TimeType.WEEKS.fromBase(base) }
    val months by lazy { TimeType.MONTHS.fromBase(base) }
    val years by lazy { TimeType.YEARS.fromBase(base) }

    /** Returns the Time's value in seconds in a new [Seconds] instance. */
    @Suppress("UNUSED")
    fun toSeconds() = Seconds(seconds)

    /** Returns the Time's value in minutes in a new [Minutes] instance. */
    @Suppress("UNUSED")
    fun toMinutes() = Minutes(minutes)

    /** Returns the Time's value in hours in a new [Hours] instance. */
    @Suppress("UNUSED")
    fun toHours() = Hours(hours)

    /** Returns the Time's value in days in a new [Days] instance. */
    @Suppress("UNUSED")
    fun toDays() = Days(days)

    /** Returns the Time's value in weeks in a new [Weeks] instance. */
    @Suppress("UNUSED")
    fun toWeeks() = Weeks(weeks)

    /** Returns the Time's value in months in a new [Months] instance. */
    @Suppress("UNUSED")
    fun toMonths() = Months(months)

    /** Returns the Time's value in year in a new [Years] instance. */
    @Suppress("UNUSED")
    fun toYears() = Years(years)

    override fun compareTo(other: Time): Int = base.compareTo(other.base)

    override fun equals(other: Any?) = other != null && other is Time && base == other.base

    override fun hashCode() = Time::class.hashCode() * 31 + value.hashCode()

    operator fun <T : Time> plus(o: Time) : T = create(type.fromBase(base + o.base))

    operator fun <T : Time> minus(o: Time): T = create(type.fromBase(base - o.base))

    operator fun <T : Time> times(o: Time): T = create(type.fromBase(base * o.base))

    operator fun <T : Time> div(o: Time): T = create(type.fromBase(base / o.base))

    operator fun <T : Time> rem(o: Time): T = create(type.fromBase(base % o.base))

    operator fun <T : Time> unaryPlus(): T = create(-value)

    operator fun <T : Time> unaryMinus(): T = create(+value)

    operator fun <T : Time> inc(): T = create(value + 1.0)

    operator fun <T : Time> dec(): T = create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : Time> plus(v: Double): T = create(value + v)

    @JsName("minusDouble")
    operator fun <T : Time> minus(v: Double): T = create(value - v)

    @JsName("timesDouble")
    operator fun <T : Time> times(v: Double): T = create(value * v)

    @JsName("divDouble")
    operator fun <T : Time> div(v: Double): T = create(value / v)

    @JsName("remDouble")
    operator fun <T : Time> rem(v: Double): T = create(value % v)
}

/**
 * Class representing [Time] in seconds.
 */
@Serializable
@SerialName("seconds")
@JsExport
class Seconds(override val value: Double = 0.0) : Time() {
    override val type: MeasureType = TimeType.SECONDS

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Seconds(v) as T
}

/**
 * Class representing [Time] in minutes.
 */
@Serializable
@SerialName("minutes")
@JsExport
class Minutes(override val value: Double = 0.0) : Time() {
    override val type: MeasureType = TimeType.MINUTES

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Minutes(v) as T
}

/**
 * Class representing [Time] in hours.
 */
@Serializable
@SerialName("hours")
@JsExport
class Hours(override val value: Double = 0.0) : Time() {
    override val type: MeasureType = TimeType.HOURS

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Hours(v) as T
}

/**
 * Class representing [Time] in days.
 */
@Serializable
@SerialName("days")
@JsExport
class Days(override val value: Double = 0.0) : Time() {
    override val type: MeasureType = TimeType. DAYS

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Days(v) as T
}


/**
 * Class representing [Time] in weeks.
 */
@Serializable
@SerialName("weeks")
@JsExport
class Weeks(override val value: Double = 0.0) : Time() {
    override val type: MeasureType = TimeType.WEEKS

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Weeks(v) as T
}

/**
 * Class representing [Time] in months.
 */
@Serializable
@SerialName("months")
@JsExport
class Months(override val value: Double = 0.0) : Time() {
    override val type: MeasureType = TimeType.MONTHS

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Months(v) as T
}

/**
 * Class representing [Time] in years.
 */
@Serializable
@SerialName("years")
@JsExport
class Years(override val value: Double = 0.0) : Time() {
    override val type: MeasureType = TimeType.YEARS

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Years(v) as T
}

/** Constants for converting [Time] values to and from base units */
const val DAYS_IN_A_YEAR = 365.0
const val MONTHS_IN_A_YEAR = 12.0
const val DAYS_IN_MONTH = DAYS_IN_A_YEAR / MONTHS_IN_A_YEAR
const val DAYS_IN_A_WEEK = 7.0
const val HOURS_IN_A_DAY = 24.0
const val MINUTES_IN_A_HOUR = 60.0
const val SECONDS_IN_A_MINUTE = 60.0
const val SECONDS_IN_A_HOUR = MINUTES_IN_A_HOUR * SECONDS_IN_A_MINUTE
const val SECONDS_IN_A_DAY = HOURS_IN_A_DAY * SECONDS_IN_A_HOUR
const val SECONDS_IN_A_WEEK = DAYS_IN_A_WEEK * SECONDS_IN_A_DAY
const val SECONDS_IN_A_MONTH = DAYS_IN_MONTH * SECONDS_IN_A_DAY
const val SECONDS_IN_A_YEAR = DAYS_IN_A_YEAR * SECONDS_IN_A_DAY

/** Functions for converting [Time] values to and from base units */
val mToS = { v: Double -> v * SECONDS_IN_A_MINUTE }
val sToM = { v: Double -> v / SECONDS_IN_A_MINUTE }
val hToS = { v: Double -> v * SECONDS_IN_A_HOUR }
val sToH = { v: Double -> v / SECONDS_IN_A_HOUR }
val dayToS = { v: Double -> v * SECONDS_IN_A_DAY }
val sToDay = { v: Double -> v / SECONDS_IN_A_DAY}
val wkToS = { v: Double -> v * SECONDS_IN_A_WEEK }
val sToWk = { v: Double -> v / SECONDS_IN_A_WEEK }
val mthToS = { v: Double -> v * SECONDS_IN_A_MONTH }
val sToMth = { v: Double -> v / SECONDS_IN_A_MONTH }
val yrToS = { v: Double -> v * SECONDS_IN_A_YEAR }
val sToYr = { v: Double -> v / SECONDS_IN_A_YEAR }

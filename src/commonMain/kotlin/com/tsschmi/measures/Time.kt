@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import com.tsschmi.measures.TimeType.*

/**
 * Enum implementing [MeasureType] to provide units of [Time] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Time] value in the common base units.
 * @param fromBase - Function that returns the [Time] value in the configured units from the base unit.
 */
@JsExport
@Serializable
sealed class TimeType(
    override val units: String,
    override val toBase : (Double) -> Double,
    override val fromBase : (Double) -> Double
) : MeasureType<Time> {

    @Serializable
    data object SecondType : TimeType("sec", identity, identity) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Second(v) as T
    }

    @Serializable
    data object MinuteType : TimeType("min", mToS, sToM) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Minute(v) as T
    }

    @Serializable
    data object HourType : TimeType("hrs", hToS, sToH) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Hour(v) as T
    }

    @Serializable
    data object DayType : TimeType("dys", dayToS, sToDay) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Day(v) as T
    }

    @Serializable
    data object WeekType : TimeType("wks", wkToS, sToWk) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Week(v) as T
    }

    @Serializable
    data object MonthType : TimeType("mos", mthToS, sToMth) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Month(v) as T
    }

    @Serializable
    data object YearType : TimeType("yrs", yrToS, sToYr) {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = Year(v) as T
    }
}

/**
 * Abstract base class extended by specific Time unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Time : BaseMeasure(), Comparable<Time> {
    /* Properties used to access this Time's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val seconds by lazy { SecondType.fromBase(base) }
    val minutes by lazy { MinuteType.fromBase(base) }
    val hours by lazy { HourType.fromBase(base) }
    val days by lazy { DayType.fromBase(base) }
    val weeks by lazy { WeekType.fromBase(base) }
    val months by lazy { MonthType.fromBase(base) }
    val years by lazy { YearType.fromBase(base) }

    override fun compareTo(other: Time): Int = base.compareTo(other.base)

    override fun equals(other: Any?) = other != null && other is Time && base == other.base

    override fun hashCode() = Time::class.hashCode() * 31 + value.hashCode()

    operator fun <T : Time> plus(o: Time) : T = type.create(type.fromBase(base + o.base))

    operator fun <T : Time> minus(o: Time): T = type.create(type.fromBase(base - o.base))

    operator fun <T : Time> times(o: Time): T = type.create(type.fromBase(base * o.base))

    operator fun <T : Time> div(o: Time): T = type.create(type.fromBase(base / o.base))

    operator fun <T : Time> rem(o: Time): T = type.create(type.fromBase(base % o.base))

    operator fun <T : Time> unaryPlus(): T = type.create(-value)

    operator fun <T : Time> unaryMinus(): T = type.create(+value)

    operator fun <T : Time> inc(): T = type.create(value + 1.0)

    operator fun <T : Time> dec(): T = type.create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : Time> plus(v: Double): T = type.create(value + v)

    @JsName("minusDouble")
    operator fun <T : Time> minus(v: Double): T = type.create(value - v)

    @JsName("timesDouble")
    operator fun <T : Time> times(v: Double): T = type.create(value * v)

    @JsName("divDouble")
    operator fun <T : Time> div(v: Double): T = type.create(value / v)

    @JsName("remDouble")
    operator fun <T : Time> rem(v: Double): T = type.create(value % v)

    @JsName("convert")
    operator fun <T : Time> invoke(type: TimeType): T = type.create(type.fromBase(base))
}

/**
 * Class representing [Time] in seconds.
 */
@Serializable
@SerialName("second")
@JsExport
class Second(override val value: Double = 0.0) : Time() {
    override val type = SecondType
}

/**
 * Class representing [Time] in minutes.
 */
@Serializable
@SerialName("minute")
@JsExport
class Minute(override val value: Double = 0.0) : Time() {
    override val type = MinuteType
}

/**
 * Class representing [Time] in hours.
 */
@Serializable
@SerialName("hour")
@JsExport
class Hour(override val value: Double = 0.0) : Time() {
    override val type = HourType
}

/**
 * Class representing [Time] in days.
 */
@Serializable
@SerialName("day")
@JsExport
class Day(override val value: Double = 0.0) : Time() {
    override val type = DayType
}


/**
 * Class representing [Time] in weeks.
 */
@Serializable
@SerialName("week")
@JsExport
class Week(override val value: Double = 0.0) : Time() {
    override val type = WeekType
}

/**
 * Class representing [Time] in months.
 */
@Serializable
@SerialName("month")
@JsExport
class Month(override val value: Double = 0.0) : Time() {
    override val type = MonthType
}

/**
 * Class representing [Time] in years.
 */
@Serializable
@SerialName("year")
@JsExport
class Year(override val value: Double = 0.0) : Time() {
    override val type = YearType
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
val sToDay = { v: Double -> v / SECONDS_IN_A_DAY }
val wkToS = { v: Double -> v * SECONDS_IN_A_WEEK }
val sToWk = { v: Double -> v / SECONDS_IN_A_WEEK }
val mthToS = { v: Double -> v * SECONDS_IN_A_MONTH }
val sToMth = { v: Double -> v / SECONDS_IN_A_MONTH }
val yrToS = { v: Double -> v * SECONDS_IN_A_YEAR }
val sToYr = { v: Double -> v / SECONDS_IN_A_YEAR }

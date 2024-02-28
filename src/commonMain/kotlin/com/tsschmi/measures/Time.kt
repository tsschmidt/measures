@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import com.tsschmi.measures.TimeType.*
import kotlin.js.JsName

/**
 * Enum implementing [MeasureType] to provide units of [Time] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Time] value in the common base units.
 * @param fromBase - Function that returns the [Time] value in the configured units from the base unit.
 */
@JsExport
@Serializable
sealed class TimeType<out T : Time>(
    override val units: String,
    override val toBase : (Double) -> Double,
    override val fromBase : (Double) -> Double,
    override val create: (Double) -> T
) : MeasureType<T> {
    @Serializable
    data object SecondType : TimeType<Second>("sec", identity, identity, ::Second)

    @Serializable
    data object MinuteType : TimeType<Minute>("min", mToS, sToM, ::Minute)

    @Serializable
    data object HourType : TimeType<Hour>("hrs", hToS, sToH, ::Hour)

    @Serializable
    data object DayType : TimeType<Day>("dys", dayToS, sToDay, ::Day)

    @Serializable
    data object WeekType : TimeType<Week>("wks", wkToS, sToWk, ::Week)

    @Serializable
    data object MonthType : TimeType<Month>("mos", mthToS, sToMth, ::Month)

    @Serializable
    data object YearType : TimeType<Year>("yrs", yrToS, sToYr, ::Year)
}

/**
 * Abstract base class extended by specific Time unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Time : Measure, Operators<Time>, Comparable<Time> {
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

    @JsName("convert")
    operator fun <T : Time> invoke(t: TimeType<T>) = t.create(t.fromBase(base))
}

/**
 * Class representing [Time] in seconds.
 */
@Serializable
@SerialName("second")
@JsExport
class Second(override val value: Double = 0.0) : Time(), MeasureOperators<Second, Time> {
    override val type = SecondType
}

/**
 * Class representing [Time] in minutes.
 */
@Serializable
@SerialName("minute")
@JsExport
class Minute(override val value: Double = 0.0) : Time(), MeasureOperators<Minute, Time> {
    override val type = MinuteType
}

/**
 * Class representing [Time] in hours.
 */
@Serializable
@SerialName("hour")
@JsExport
class Hour(override val value: Double = 0.0) : Time(), MeasureOperators<Hour, Time> {
    override val type = HourType
}

/**
 * Class representing [Time] in days.
 */
@Serializable
@SerialName("day")
@JsExport
class Day(override val value: Double = 0.0) : Time(), MeasureOperators<Day, Time> {
    override val type = DayType
}


/**
 * Class representing [Time] in weeks.
 */
@Serializable
@SerialName("week")
@JsExport
class Week(override val value: Double = 0.0) : Time(), MeasureOperators<Week, Time> {
    override val type = WeekType
}

/**
 * Class representing [Time] in months.
 */
@Serializable
@SerialName("month")
@JsExport
class Month(override val value: Double = 0.0) : Time(), MeasureOperators<Month, Time> {
    override val type = MonthType
}

/**
 * Class representing [Time] in years.
 */
@Serializable
@SerialName("year")
@JsExport
class Year(override val value: Double = 0.0) : Time(), MeasureOperators<Year, Time> {
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

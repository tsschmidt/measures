@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

/**
 * Enum implementing [MeasureType] to provide units of [Concentration] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Concentration] value in the common base units.
 * @param fromBase - Function that returns the [Concentration] value in the configured units from the base unit.
 */
@JsExport
@Serializable
sealed class ConcentrationType<out T : Concentration>(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double,
    override val create: (Double) -> T
) : MeasureType<T> {
    @Serializable
    data object PartsPerMillionType : ConcentrationType<PartsPerMillion>("ppm", identity, identity, ::PartsPerMillion)

    @Serializable
    data object PartsPerBillionType: ConcentrationType<PartsPerBillion>("ppb", identity, identity, ::PartsPerBillion)

    @Serializable
    data object MassPercentType: ConcentrationType<MassPercent>("m/m%", identity, identity, ::MassPercent)

    @Serializable
    data object VolumePercentType: ConcentrationType<VolumePercent>("v/v%", identity, identity, ::VolumePercent)
}

/**
 * Abstract base class extended by specific Gravity unit classes.
 */
@Serializable
@JsExport
sealed class Concentration : Measure, Operators<Concentration>, Comparable<Concentration> {
    override val base by lazy { type.toBase(value) }

    override fun equals(other: Any?) = other != null && other is Concentration && base == other.base

    override fun hashCode(): Int = Concentration::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Concentration): Int = base.compareTo(other.base)

    @JsName("convert")
    operator fun <T : Concentration> invoke(d: ConcentrationType<T>) = d.create(d.fromBase(base))

    /** inc() and dec() implemented here instead of interface because of a Kotlin generics issue */
    @JsExport.Ignore
    operator fun inc() = this(value + 1.0)

    @JsExport.Ignore
    operator fun dec() = this(value - 1.0)
}

/**
 * Class representing a [Concentration] value in parts per million.
 */
@Serializable
@SerialName("ppm")
@JsExport
class PartsPerMillion(override val value: Double = 0.0) : Concentration(), MeasureOperators<PartsPerMillion, Concentration> {
    override val type = ConcentrationType.PartsPerMillionType
}

@Serializable
@SerialName("ppb")
@JsExport
class PartsPerBillion(override val value: Double = 0.0) : Concentration(), MeasureOperators<PartsPerBillion, Concentration> {
    override val type = ConcentrationType.PartsPerBillionType
}

@Serializable
@SerialName("mass_percent")
@JsExport
class MassPercent(override val value: Double = 0.0) : Concentration(), MeasureOperators<MassPercent, Concentration> {
    override val type = ConcentrationType.MassPercentType
}

@Serializable
@SerialName("volume_percent")
@JsExport
class VolumePercent(override  val value: Double = 0.0) : Concentration(), MeasureOperators<VolumePercent, Concentration> {
    override val type = ConcentrationType.VolumePercentType
}
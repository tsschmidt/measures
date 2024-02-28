@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Enum implementing [MeasureType] to provide units of [Dilution] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Dilution] value in the common base units.
 * @param fromBase - Function that returns the [Dilution] value in the configured units from the base unit.
 */
@JsExport
@Serializable
sealed class DilutionType(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double
) : MeasureType {
    @Serializable
    data object PartsPerMillionType : DilutionType("ppm", identity, identity)
}

/**
 * Abstract base class extended by specific Gravity unit classes.
 */
@Serializable
@JsExport
sealed class Dilution : Measure, Comparable<Dilution> {
    //abstract fun <T : Dilution> create(value: Double): T = PPM(value) as T
    override val base by lazy { type.toBase(value) }

    override fun equals(other: Any?) = other != null && other is Dilution && base == other.base

    override fun hashCode(): Int = Dilution::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Dilution): Int = base.compareTo(other.base)
}

/**
 * Class representing a [Dilution] value in parts per million.
 */
@Serializable
@SerialName("ppm")
@JsExport
class PartsPerMillion(override val value: Double = 0.0) : Dilution(), MeasureOperators<PartsPerMillion, Dilution> {
    override val type: MeasureType = DilutionType.PartsPerMillionType
    override fun create(v: Double) = PartsPerMillion(v)
}


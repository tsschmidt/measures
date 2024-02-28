@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

/**
 * Enum implementing [MeasureType] to provide units of [Dilution] being represented with functions for creating and converting.
 *
 * @param units - String used to display units
 * @param toBase - Function that returns the [Dilution] value in the common base units.
 * @param fromBase - Function that returns the [Dilution] value in the configured units from the base unit.
 */
@JsExport
@Serializable
sealed class DilutionType<out T : Dilution>(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double,
    override val create: (Double) -> T
) : MeasureType<T> {
    @Serializable
    data object PartsPerMillionType : DilutionType<PartsPerMillion>("ppm", identity, identity, ::PartsPerMillion)
}

/**
 * Abstract base class extended by specific Gravity unit classes.
 */
@Serializable
@JsExport
sealed class Dilution : Measure, Operators<Dilution>, Comparable<Dilution> {
    override val base by lazy { type.toBase(value) }

    override fun equals(other: Any?) = other != null && other is Dilution && base == other.base

    override fun hashCode(): Int = Dilution::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Dilution): Int = base.compareTo(other.base)

    @JsName("convert")
    operator fun <T : Dilution> invoke(d: DilutionType<T>) = d.create(d.fromBase(base))
}

/**
 * Class representing a [Dilution] value in parts per million.
 */
@Serializable
@SerialName("ppm")
@JsExport
class PartsPerMillion(override val value: Double = 0.0) : Dilution(), MeasureOperators<PartsPerMillion, Dilution> {
    override val type = DilutionType.PartsPerMillionType
}


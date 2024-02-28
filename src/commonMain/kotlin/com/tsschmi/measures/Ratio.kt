@file:OptIn(ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

/**
 * Enum to identify the type of [Ratio].
 */
@JsExport
@Serializable
sealed class RatioType(
    override val units: String = "",
    override val toBase: (Double) -> Double = identity,
    override val fromBase: (Double) -> Double = identity
) : MeasureType {
    data object WeightToVolumeType : RatioType()
    data object VolumeToWeightType : RatioType()
    data object FlowType : RatioType()
    data object VelocityType : RatioType()
}

/**
 * Abstract base class extended by specific Ratio unit classes.
 */
@Serializable
@JsExport
@Suppress("UNUSED")
sealed class Ratio : Measure {
    override val base by lazy { type.toBase(value) }
    val units by lazy { "${n.units}/${d.units}" }

    abstract val n : MeasureType

    abstract val d : MeasureType

    fun toBase(v: Double) = n.toBase(value) / d.toBase(1.0)

    fun fromBase(v: Double) = n.fromBase(value) / d.fromBase(1.0)

    /**
    * Function returns the value of this Ratio in the form passed.  Numerator and denominator types
    * must match or an error is thrown.
    *
    * @param num - [MeasureType] of numerator to convert.
    * @param den - [MeasureType] of denominator to convert.
    */
    fun convert(num: MeasureType, den: MeasureType) = num.fromBase(base) / den.fromBase(1.0)
}

/**
 * Class representing a [Ratio] in the form of [Ratio] to [Volume].
 *
 * @param - value of the [Ratio]
 * @param - [RatioType] for the numerator of the ratio.
 * @param - [VolumeType] for the denominator of the ratio.
 */
@Serializable
@SerialName("WeightToVolume")
@JsExport
class WeightToVolume(
    override val value: Double = 0.0,
    override val n: WeightType,
    override val d: VolumeType
) : Ratio(), MeasureOperators<WeightToVolume, WeightToVolume>, Comparable<WeightToVolume> {

    override val type: MeasureType = RatioType.WeightToVolumeType

    override fun equals(other: Any?) = other != null && other is WeightToVolume && base == other.base

    override fun hashCode() = this::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: WeightToVolume): Int = base.compareTo(other.base)

    @JsName("toWeightToVolume")
    operator fun invoke(n: WeightType, d: VolumeType) = WeightToVolume(convert(n, d), n, d)

    override fun create(v: Double) = WeightToVolume(v, n, d)
}

/**
 * Class representing a [Ratio] in the form of [Volume] to [Ratio].
 *
 * @param - value of the [Ratio]
 * @param - [VolumeType] for the numerator of the ratio.
 * @param - [RatioType] for the denominator of the ratio.
 */
@Serializable
@SerialName("volumeToWeight")
@JsExport
class VolumeToWeight(
    override val value: Double = 0.0,
    override val n: VolumeType,
    override val d: WeightType
) : Ratio(), MeasureOperators<VolumeToWeight, VolumeToWeight>, Comparable<VolumeToWeight> {

    override val type: MeasureType = RatioType.VolumeToWeightType

    override fun equals(other: Any?) = other != null && other is VolumeToWeight && base == other.base

    override fun hashCode() = this::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: VolumeToWeight): Int = base.compareTo(other.base)

    @JsName("convertVolumeToWeight")
    operator fun invoke(n: VolumeType, d: WeightType) = VolumeToWeight(convert(n, d), n, d)

    override fun create(v: Double) = VolumeToWeight(v, n, d)
}

/**
 * Class representing a [Ratio] in the form of [Volume] to [Time].
 *
 * @param - value of the [Ratio]
 * @param - [VolumeType] for the numerator of the ratio.
 * @param - [TimeType] for the denominator of the ratio.
 */
@Serializable
@SerialName("flow")
@JsExport
class Flow(
    override val value: Double = 0.0,
    override val n: VolumeType,
    override val d: TimeType
) : Ratio(), MeasureOperators<Flow, Flow>, Comparable<Flow> {

    override val type: MeasureType = RatioType.FlowType

    override fun equals(other: Any?) = other != null && other is Flow && base == other.base

    override fun hashCode() = this::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Flow): Int = base.compareTo(other.base)

    @JsName("convertFlow")
    operator fun invoke(n: VolumeType, d: TimeType) = Flow(convert(n, d), n, d)

    override fun create(v: Double) = Flow(v, n, d)
}

/**
 * Class representing a [Ratio] in the form of [Length] to [Time].
 *
 * @param - value of the [Ratio]
 * @param - [VolumeType] for the numerator of the ratio.
 * @param - [TimeType] for the denominator of the ratio.
 */
@Serializable
@SerialName("velocity")
@JsExport
class Velocity(
    override val value: Double = 0.0,
    override val n: LengthType,
    override val d: TimeType
) : Ratio(), MeasureOperators<Velocity, Velocity>, Comparable<Velocity> {

    override val type: MeasureType = RatioType.VelocityType

    override fun equals(other: Any?) = other != null && other is Velocity && base == other.base

    override fun hashCode() = this::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Velocity): Int = base.compareTo(other.base)

    @JsName("convertVelocity")
    operator fun invoke(n: LengthType, d: TimeType) = Velocity(convert(n, d), n, d)

    override fun create(v: Double) = Velocity(v, n, d)
}

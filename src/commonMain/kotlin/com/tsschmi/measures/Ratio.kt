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
) : MeasureType<Ratio> {

    @Serializable
    data object WeightToVolumeType : RatioType()

    @Serializable
    data object VolumeToWeightType : RatioType()

    @Serializable
    data object FlowType : RatioType()

    @Serializable
    data object VelocityType : RatioType()

    override fun <T> create(v: Double): T {
        TODO("Not yet implemented")
    }
}

/**
 * Abstract base class extended by specific Ratio unit classes.
 */
@Serializable
@JsExport
sealed class Ratio : BaseMeasure() {
    override val base by lazy { toBase(value) }
    val units by lazy { "${n.units}/${d.units}" }

    abstract val n : MeasureType<*>

    abstract val d : MeasureType<*>

    fun toBase(v: Double) = n.toBase(value) / d.toBase(1.0)

    abstract fun <T : Ratio> create(v: Double): T

    fun fromBase(v: Double) = n.fromBase(value) / d.fromBase(1.0)

    /**
    * Function returns the value of this Ratio in the form passed.  Numerator and denominator types
    * must match or an error is thrown.
    *
    * @param num - [MeasureType] of numerator to convert.
    * @param den - [MeasureType] of denominator to convert.
    */
    fun convert(num: MeasureType<*>, den: MeasureType<*>) = num.fromBase(base) / den.fromBase(1.0)

    operator fun <T : Ratio> unaryPlus(): T = create(-value)

    operator fun <T : Ratio> unaryMinus(): T = create(+value)

    operator fun <T : Ratio> inc(): T = create(value + 1.0)

    operator fun <T : Ratio> dec(): T = create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : Ratio> plus(v: Double): T = create(value + v)

    @JsName("minusDouble")
    operator fun <T : Ratio> minus(v: Double): T = create(value - v)

    @JsName("timesDouble")
    operator fun <T : Ratio> times(v: Double): T = create(value * v)

    @JsName("divDouble")
    operator fun <T : Ratio> div(v: Double): T = create(value / v)

    @JsName("remDouble")
    operator fun <T : Ratio> rem(v: Double): T = create(value % v)

    override fun display(d: Int): String = "${format(d)}${units}"
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
) : Ratio(), Comparable<WeightToVolume> {

    override val type = RatioType.WeightToVolumeType

    override fun equals(other: Any?) = other != null && other is WeightToVolume && base == other.base

    override fun hashCode() = this::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: WeightToVolume): Int = base.compareTo(other.base)

    operator fun <T : WeightToVolume> plus(o: WeightToVolume) : T = create(fromBase(base + o.base))

    operator fun <T : WeightToVolume> minus(o: WeightToVolume): T = create(fromBase(base - o.base))

    operator fun <T : WeightToVolume> times(o: WeightToVolume): T = create(fromBase(base * o.base))

    operator fun <T : WeightToVolume> div(o: WeightToVolume): T = create(fromBase(base / o.base))

    operator fun <T : WeightToVolume> rem(o: WeightToVolume): T = create(fromBase(base % o.base))

    @JsName("convertWeightToVolume")
    @Suppress("UNCHECKED_CAST")
    operator fun <T : WeightToVolume> invoke(n: WeightType, d: VolumeType): T = WeightToVolume(convert(n, d), n, d) as T

    @Suppress("UNCHECKED_CAST")
    override fun <T : Ratio> create(v: Double): T = WeightToVolume(v, n, d) as T
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
) : Ratio(), Comparable<VolumeToWeight> {

    override val type = RatioType.VolumeToWeightType

    override fun equals(other: Any?) = other != null && other is VolumeToWeight && base == other.base

    override fun hashCode() = this::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: VolumeToWeight): Int = base.compareTo(other.base)

    operator fun <T : VolumeToWeight> plus(o: VolumeToWeight) : T = create(fromBase(base + o.base))

    operator fun <T : VolumeToWeight> minus(o: VolumeToWeight): T = create(fromBase(base - o.base))

    operator fun <T : VolumeToWeight> times(o: VolumeToWeight): T = create(fromBase(base * o.base))

    operator fun <T : VolumeToWeight> div(o: VolumeToWeight): T = create(fromBase(base / o.base))

    operator fun <T : VolumeToWeight> rem(o: VolumeToWeight): T = create(fromBase(base % o.base))

    @JsName("convertVolumeToWeight")
    @Suppress("UNCHECKED_CAST")
    operator fun <T : VolumeToWeight> invoke(n: VolumeType, d: WeightType): T = VolumeToWeight(convert(n, d), n, d) as T

    @Suppress("UNCHECKED_CAST")
    override fun <T : Ratio> create(v: Double): T = VolumeToWeight(v, n, d) as T
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
) : Ratio(), Comparable<Flow> {

    override val type = RatioType.FlowType

    override fun equals(other: Any?) = other != null && other is Flow && base == other.base

    override fun hashCode() = this::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Flow): Int = base.compareTo(other.base)

    operator fun <T : Flow> plus(o: Flow) : T = create(fromBase(base + o.base))

    operator fun <T : Flow> minus(o: Flow): T = create(fromBase(base - o.base))

    operator fun <T : Flow> times(o: Flow): T = create(fromBase(base * o.base))

    operator fun <T : Flow> div(o: Flow): T = create(fromBase(base / o.base))

    operator fun <T : Flow> rem(o: Flow): T = create(fromBase(base % o.base))

    @JsName("convertFlow")
    @Suppress("UNCHECKED_CAST")
    operator fun <T : Flow> invoke(n: VolumeType, d: TimeType): T = Flow(convert(n, d), n, d) as T

    @Suppress("UNCHECKED_CAST")
    override fun <T : Ratio> create(v: Double): T = Flow(v, n, d) as T
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
) : Ratio(), Comparable<Velocity> {

    override val type = RatioType.VelocityType

    override fun equals(other: Any?) = other != null && other is Velocity && base == other.base

    override fun hashCode() = this::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Velocity): Int = base.compareTo(other.base)

    operator fun <T : Velocity> plus(o: Velocity) : T = create(fromBase(base + o.base))

    operator fun <T : Velocity> minus(o: Velocity): T = create(fromBase(base - o.base))

    operator fun <T : Velocity> times(o: Velocity): T = create(fromBase(base * o.base))

    operator fun <T : Velocity> div(o: Velocity): T = create(fromBase(base / o.base))

    operator fun <T : Velocity> rem(o: Velocity): T = create(fromBase(base % o.base))

    @JsName("convertVelocity")
    @Suppress("UNCHECKED_CAST")
    operator fun <T : Velocity> invoke(n: LengthType, d: TimeType): T = Velocity(convert(n, d), n, d) as T

    @Suppress("UNCHECKED_CAST")
    override fun <T : Ratio> create(v: Double): T = Velocity(v, n, d) as T
}

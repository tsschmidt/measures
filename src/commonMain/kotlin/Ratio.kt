@file:OptIn(ExperimentalJsExport::class)

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

/**
 * Enum to identify the type of [Ratio].
 */
@JsExport
enum class RatioType  {
    WEIGHT_VOLUME, VOLUME_WEIGHT, FLOW
}

/**
 * Generic function to create a [MeasureType] for the [Ratio] being represented.
 *
 * @param n - [MeasureType] for the numerator of the [Ratio].
 * @param d - [MeasureType] for the denominator of the [Ratio].
 */
@JsExport
fun <N : Measure, D : Measure>createRatioType(
    n: MeasureType<N>,
    d: MeasureType<D>,
    create: (Double) -> Ratio
): MeasureType<Ratio> {
    return object : MeasureType<Ratio> {
        override val units = "${n.units}/${d.units}"
        override val toBase = { value: Double ->  n.toBase(value) / d.toBase(1.0) }
        override val fromBase = { value: Double -> n.fromBase(value) / d.fromBase(1.0) }
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(v: Double): T = create(v) as T
    }
}

/**
 * Abstract base class extended by specific Ratio unit classes.
 *
 * @param ratioType - [RatioType] for representing the specific form of the Ratio.
 */
@Serializable
@JsExport
sealed class Ratio(val ratioType: RatioType, override val type: MeasureType<Ratio>) : BaseMeasure() {
    override val base by lazy { type.toBase(value) }

    /**
     * Function returns the value of this Ratio in the form passed.  Numerator and denominator types
     * must match or an error is thrown.
     *
     * @param num - [MeasureType] of numerator to convert.
     * @param den - [MeasureType] of denominator to convert.
     */
     fun convert(num: MeasureType<*>, den: MeasureType<*>) = num.fromBase(base) / den.fromBase(1.0)

    operator fun <T : Ratio> unaryPlus(): T = type.create(-value)

    operator fun <T : Ratio> unaryMinus(): T = type.create(+value)

    operator fun <T : Ratio> inc(): T = type.create(value + 1.0)

    operator fun <T : Ratio> dec(): T = type.create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : Ratio> plus(v: Double): T = type.create(value + v)

    @JsName("minusDouble")
    operator fun <T : Ratio> minus(v: Double): T = type.create(value - v)

    @JsName("timesDouble")
    operator fun <T : Ratio> times(v: Double): T = type.create(value * v)

    @JsName("divDouble")
    operator fun <T : Ratio> div(v: Double): T = type.create(value / v)

    @JsName("remDouble")
    operator fun <T : Ratio> rem(v: Double): T = type.create(value % v)
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
    val n: WeightType,
    val d: VolumeType
) : Ratio(RatioType.WEIGHT_VOLUME, createRatioType(n, d) { v: Double -> WeightToVolume(v, n, d) }), Comparable<WeightToVolume> {

    override fun equals(other: Any?) = other != null && other is WeightToVolume && base == other.base

    override fun hashCode() = this::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: WeightToVolume): Int = base.compareTo(other.base)

    operator fun <T : WeightToVolume> plus(o: WeightToVolume) : T = type.create(type.fromBase(base + o.base))

    operator fun <T : WeightToVolume> minus(o: WeightToVolume): T = type.create(type.fromBase(base - o.base))

    operator fun <T : WeightToVolume> times(o: WeightToVolume): T = type.create(type.fromBase(base * o.base))

    operator fun <T : WeightToVolume> div(o: WeightToVolume): T = type.create(type.fromBase(base / o.base))

    operator fun <T : WeightToVolume> rem(o: WeightToVolume): T = type.create(type.fromBase(base % o.base))

    @JsName("convertWeightToVolume")
    @Suppress("UNCHECKED_CAST")
    operator fun <T : WeightToVolume> invoke(n: WeightType, d: VolumeType): T = WeightToVolume(convert(n, d), n, d) as T
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
    val n: VolumeType,
    val d: WeightType
) : Ratio(RatioType.VOLUME_WEIGHT, createRatioType(n, d) { v: Double -> VolumeToWeight(v, n, d) }), Comparable<VolumeToWeight> {

    override fun equals(other: Any?) = other != null && other is VolumeToWeight && base == other.base

    override fun hashCode() = this::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: VolumeToWeight): Int = base.compareTo(other.base)

    operator fun <T : VolumeToWeight> plus(o: VolumeToWeight) : T = type.create(type.fromBase(base + o.base))

    operator fun <T : VolumeToWeight> minus(o: VolumeToWeight): T = type.create(type.fromBase(base - o.base))

    operator fun <T : VolumeToWeight> times(o: VolumeToWeight): T = type.create(type.fromBase(base * o.base))

    operator fun <T : VolumeToWeight> div(o: VolumeToWeight): T = type.create(type.fromBase(base / o.base))

    operator fun <T : VolumeToWeight> rem(o: VolumeToWeight): T = type.create(type.fromBase(base % o.base))

    @JsName("convertVolumeToWeight")
    @Suppress("UNCHECKED_CAST")
    operator fun <T : VolumeToWeight> invoke(n: VolumeType, d: WeightType): T = VolumeToWeight(convert(n, d), n, d) as T
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
    val n: VolumeType,
    val d: TimeType
) : Ratio(RatioType.FLOW, createRatioType(n, d) { v: Double -> Flow(v, n, d)} ), Comparable<Flow> {

    override fun equals(other: Any?) = other != null && other is Flow && base == other.base

    override fun hashCode() = this::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Flow): Int = base.compareTo(other.base)

    operator fun <T : Flow> plus(o: Flow) : T = type.create(type.fromBase(base + o.base))

    operator fun <T : Flow> minus(o: Flow): T = type.create(type.fromBase(base - o.base))

    operator fun <T : Flow> times(o: Flow): T = type.create(type.fromBase(base * o.base))

    operator fun <T : Flow> div(o: Flow): T = type.create(type.fromBase(base / o.base))

    operator fun <T : Flow> rem(o: Flow): T = type.create(type.fromBase(base % o.base))

    @JsName("convertFlow")
    @Suppress("UNCHECKED_CAST")
    operator fun <T : Flow> invoke(n: VolumeType, d: TimeType): T = Flow(convert(n, d), n, d) as T
}

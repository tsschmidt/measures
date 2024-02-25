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
fun createRatioType(
    n: MeasureType,
    d: MeasureType,
): MeasureType {
    return object : MeasureType {
        override val units = "${n.units}/${d.units}"
        override val toBase = { value: Double ->  n.toBase(value) / d.toBase(1.0) }
        override val fromBase = { value: Double -> n.fromBase(value) / d.fromBase(1.0) }
    }
}

/**
 * Abstract base class extended by specific Ratio unit classes.
 *
 * @param ratioType - [RatioType] for representing the specific form of the Ratio.
 */
@Serializable
@JsExport
sealed class Ratio(val ratioType: RatioType) : BaseMeasure() {
    override val base by lazy { type.toBase(value) }

    /**
     * Function returns the value of this Ratio in the form passed.  Numerator and denominator types
     * must match or an error is thrown.
     *
     * @param num - [MeasureType] of numerator to convert.
     * @param den - [MeasureType] of denominator to convert.
     */
     fun convert(num: MeasureType, den: MeasureType) = num.fromBase(base) / den.fromBase(1.0)
/*
    operator fun <T : Ratio> plus(o: Ratio) : T = create(type.fromBase(base + o.base))

    operator fun <T : Ratio> minus(o: Ratio): T = create(type.fromBase(base - o.base))

    operator fun <T : Ratio> times(o: Ratio): T = create(type.fromBase(base * o.base))

    operator fun <T : Ratio> div(o: Ratio): T = create(type.fromBase(base / o.base))

    operator fun <T : Ratio> rem(o: Ratio): T = create(type.fromBase(base % o.base))

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
*/
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
    val d: VolumeType,
    override val type: MeasureType = createRatioType(n, d)
) : Ratio(RatioType.WEIGHT_VOLUME), Comparable<WeightToVolume> {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = WeightToVolume(v, n, d) as T

    @Suppress("UNUSED")
    /**
     * Function returns a new [WeightToVolume] converted to the unit [MeasureType]s passed.
     *
     * @param num - New [RatioType] unit for the numerator.
     * @param den - New [VolumeType] unit for the denominator.
     */
    fun to(num: WeightType, den: VolumeType) = WeightToVolume( convert(num, den), num, den)

    override fun equals(other: Any?) = other != null && other is WeightToVolume && base == other.base

    override fun hashCode() = this::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: WeightToVolume): Int = base.compareTo(other.base)

    operator fun <T : WeightToVolume> plus(o: WeightToVolume) : T = create(type.fromBase(base + o.base))

    operator fun <T : WeightToVolume> minus(o: WeightToVolume): T = create(type.fromBase(base - o.base))

    operator fun <T : WeightToVolume> times(o: WeightToVolume): T = create(type.fromBase(base * o.base))

    operator fun <T : WeightToVolume> div(o: WeightToVolume): T = create(type.fromBase(base / o.base))

    operator fun <T : WeightToVolume> rem(o: WeightToVolume): T = create(type.fromBase(base % o.base))

    operator fun <T : WeightToVolume> unaryPlus(): T = create(-value)

    operator fun <T : WeightToVolume> unaryMinus(): T = create(+value)

    operator fun <T : WeightToVolume> inc(): T = create(value + 1.0)

    operator fun <T : WeightToVolume> dec(): T = create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : WeightToVolume> plus(v: Double): T = create(value + v)

    @JsName("minusDouble")
    operator fun <T : WeightToVolume> minus(v: Double): T = create(value - v)

    @JsName("timesDouble")
    operator fun <T : WeightToVolume> times(v: Double): T = create(value * v)

    @JsName("divDouble")
    operator fun <T : WeightToVolume> div(v: Double): T = create(value / v)

    @JsName("remDouble")
    operator fun <T : WeightToVolume> rem(v: Double): T = create(value % v)
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
    val d: WeightType,
    override val type: MeasureType = createRatioType(n, d)
) : Ratio(RatioType.VOLUME_WEIGHT), Comparable<VolumeToWeight> {

    @Suppress("UNUSED")
    /**
     * Function returns a new [VolumeToWeight] converted to the unit [MeasureType]s passed.
     *
     * @param num - New [VolumeType] unit for the numerator.
     * @param den- New [RatioType] unit for the denominator.
     */
    fun to(num: VolumeType, den: WeightType) = VolumeToWeight(convert(num, den), num, den)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = VolumeToWeight(v, n, d) as T

    override fun equals(other: Any?) = other != null && other is VolumeToWeight && base == other.base

    override fun hashCode() = this::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: VolumeToWeight): Int = base.compareTo(other.base)

    operator fun <T : VolumeToWeight> plus(o: VolumeToWeight) : T = create(type.fromBase(base + o.base))

    operator fun <T : VolumeToWeight> minus(o: VolumeToWeight): T = create(type.fromBase(base - o.base))

    operator fun <T : VolumeToWeight> times(o: VolumeToWeight): T = create(type.fromBase(base * o.base))

    operator fun <T : VolumeToWeight> div(o: VolumeToWeight): T = create(type.fromBase(base / o.base))

    operator fun <T : VolumeToWeight> rem(o: VolumeToWeight): T = create(type.fromBase(base % o.base))

    operator fun <T : VolumeToWeight> unaryPlus(): T = create(-value)

    operator fun <T : VolumeToWeight> unaryMinus(): T = create(+value)

    operator fun <T : VolumeToWeight> inc(): T = create(value + 1.0)

    operator fun <T : VolumeToWeight> dec(): T = create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : VolumeToWeight> plus(v: Double): T = create(value + v)

    @JsName("minusDouble")
    operator fun <T : VolumeToWeight> minus(v: Double): T = create(value - v)

    @JsName("timesDouble")
    operator fun <T : VolumeToWeight> times(v: Double): T = create(value * v)

    @JsName("divDouble")
    operator fun <T : VolumeToWeight> div(v: Double): T = create(value / v)

    @JsName("remDouble")
    operator fun <T : VolumeToWeight> rem(v: Double): T = create(value % v)
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
    val d: TimeType,
    override val type: MeasureType = createRatioType(n, d)
) : Ratio(RatioType.FLOW), Comparable<Flow> {


    /**
     * Function returns a new [Flow] converted to the unit [MeasureType]s passed.
     *
     * @param num - New [VolumeType] unit for the numerator.
     * @param den - New [TimeType] unit for the denominator.
     */
    @Suppress("UNUSED")
    fun to(num: VolumeType, den: TimeType) = Flow(convert(num, den), num, den)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Flow(v, n, d) as T

    override fun equals(other: Any?) = other != null && other is Flow && base == other.base

    override fun hashCode() = this::class.hashCode() * 31 + base.hashCode()

    override fun compareTo(other: Flow): Int = base.compareTo(other.base)

    operator fun <T : Flow> plus(o: Flow) : T = create(type.fromBase(base + o.base))

    operator fun <T : Flow> minus(o: Flow): T = create(type.fromBase(base - o.base))

    operator fun <T : Flow> times(o: Flow): T = create(type.fromBase(base * o.base))

    operator fun <T : Flow> div(o: Flow): T = create(type.fromBase(base / o.base))

    operator fun <T : Flow> rem(o: Flow): T = create(type.fromBase(base % o.base))

    operator fun <T : Flow> unaryPlus(): T = create(-value)

    operator fun <T : Flow> unaryMinus(): T = create(+value)

    operator fun <T : Flow> inc(): T = create(value + 1.0)

    operator fun <T : Flow> dec(): T = create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : Flow> plus(v: Double): T = create(value + v)

    @JsName("minusDouble")
    operator fun <T : Flow> minus(v: Double): T = create(value - v)

    @JsName("timesDouble")
    operator fun <T : Flow> times(v: Double): T = create(value * v)

    @JsName("divDouble")
    operator fun <T : Flow> div(v: Double): T = create(value / v)

    @JsName("remDouble")
    operator fun <T : Flow> rem(v: Double): T = create(value % v)
}

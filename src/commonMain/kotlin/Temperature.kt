@file:OptIn(ExperimentalJsExport::class)

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

@JsExport
enum class TemperatureType(
    override val units: String,
    override val toBase: (Double) -> Double,
    override val fromBase: (Double) -> Double
) : MeasureType {
    FAHRENHEIT("\u00B0F", identity, identity),
    CELSIUS("\u00B0C", ctoF, fToC)
}

/**
 * Abstract base class extended by specific Temperature unit classes.
 */
@Serializable
@JsExport
sealed class Temperature : BaseMeasure(), Comparable<Temperature> {
    /* Properties used to access this Temperature's value in all available units.  Initialized lazily on first access. */
    override val base by lazy { type.toBase(value) }
    val fahrenheit by lazy { TemperatureType.FAHRENHEIT.fromBase(base) }
    val celsius by lazy { TemperatureType.CELSIUS.fromBase(base) }

    /** Returns the Temperature's value in seconds in a new [Fahrenheit] instance. */
    @Suppress("UNUSED")
    fun toFahrenheit() = Fahrenheit(fahrenheit)

    /** Returns the Temperature's value in seconds in a new [Celsius] instance. */
    @Suppress("UNUSED")
    fun toCelsius() = Celsius(celsius)

    override fun compareTo(other: Temperature): Int = base.compareTo(other.base)

    override fun equals(other: Any?) = other != null && other is Temperature && base == other.base

    override fun hashCode(): Int = Temperature::class.hashCode() * 31 + base.hashCode()

    operator fun <T : Temperature> plus(o: Temperature) : T = create(type.fromBase(base + o.base))

    operator fun <T : Temperature> minus(o: Temperature): T = create(type.fromBase(base - o.base))

    operator fun <T : Temperature> times(o: Temperature): T = create(type.fromBase(base * o.base))

    operator fun <T : Temperature> div(o: Temperature): T = create(type.fromBase(base / o.base))

    operator fun <T : Temperature> rem(o: Temperature): T = create(type.fromBase(base % o.base))

    operator fun <T : Temperature> unaryPlus(): T = create(-value)

    operator fun <T : Temperature> unaryMinus(): T = create(+value)

    operator fun <T : Temperature> inc(): T = create(value + 1.0)

    operator fun <T : Temperature> dec(): T = create(value - 1.0)

    @JsName("plusDouble")
    operator fun <T : Temperature> plus(v: Double): T = create(value + v)

    @JsName("minusDouble")
    operator fun <T : Temperature> minus(v: Double): T = create(value - v)

    @JsName("timesDouble")
    operator fun <T : Temperature> times(v: Double): T = create(value * v)

    @JsName("divDouble")
    operator fun <T : Temperature> div(v: Double): T = create(value / v)

    @JsName("remDouble")
    operator fun <T : Temperature> rem(v: Double): T = create(value % v)
}

/**
 * Class representing [Temperature] in Fahrenheit.
 */
@Serializable
@SerialName("fahrenheit")
@JsExport
class Fahrenheit(override val value: Double = 0.0) : Temperature() {
    override val type: MeasureType = TemperatureType.FAHRENHEIT

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Fahrenheit(v) as T
}
/**
 * Class representing [Temperature] in Celsius.
 */
@Serializable
@SerialName("celsius")
@JsExport
class Celsius(override val value: Double = 0.0) : Temperature() {
    override val type: MeasureType = TemperatureType.CELSIUS

    @Suppress("UNCHECKED_CAST")
    override fun <T : Measure> create(v: Double): T = Celsius(v) as T
}

/** Functions for converting [Temperature] values form base units to other units. */
val ctoF = { v: Double -> (v * 9.0 / 5.0) + 32.0 }
val fToC = { v: Double -> (v - 32.0) * 5.0 / 9.0 }

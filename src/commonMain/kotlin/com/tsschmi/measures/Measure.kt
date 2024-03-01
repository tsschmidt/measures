@file:OptIn(ExperimentalSerializationApi::class, ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.Serializable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

@JsExport
@Serializable
sealed interface MeasureType<out T : Measure> {
    val units: String
    val toBase: (Double) -> (Double)
    val fromBase: (Double) -> (Double)
    val create: (v: Double) -> T
}

/**
 * Defining interface for all implementations.
 */
@JsExport
@Serializable
@JsonClassDiscriminator("class")
sealed interface Measure {
    val value: Double
    val base: Double
    val type: MeasureType<*>
    fun display(d: Int): String = "${format(d)}${type.units}"
    fun format(d: Int = 2): String = formatNumber(value, d)

    @JsExport.Ignore()
    operator fun invoke(): Double = value

    @JsExport.Ignore()
    operator fun invoke(v: Double): Measure
}


sealed interface Operators<R : Measure> : Measure {

    @JsExport.Ignore()
    private fun units(o: R) = if (type === o.type) o() else type.fromBase(o.base)

    @JsExport.Ignore()
    operator fun plus(o: R) = this(this() + units(o))

    @JsExport.Ignore()
    operator fun minus(o: R) = this(this() - units(o))

    @JsExport.Ignore()
    operator fun times(o: R) = this(this() * units(o))

    @JsExport.Ignore()
    operator fun div(o: R) = this(this() / units(o))

    @JsExport.Ignore()
    operator fun rem(o: R) = this(this() % units(o))

    @JsExport.Ignore()
    operator fun unaryPlus() = this(-value)

    @JsExport.Ignore()
    operator fun unaryMinus() = this(+value)

    @JsExport.Ignore()
    operator fun plus(v: Double) = this(value + v)

    @JsExport.Ignore()
    operator fun minus(v: Double) = this(value - v)

    @JsExport.Ignore()
    operator fun times(v: Double) = this(value * v)

    @JsExport.Ignore()
    operator fun div(v: Double) = this(value / v)

    @JsExport.Ignore()
    operator fun rem(v: Double) = this(value % v)

    @JsExport.Ignore()
    @Suppress("UNCHECKED_CASt")
    override fun invoke(v: Double): R = (type as MeasureType<R>).create(v)

}

@Suppress("UNCHECKED_CAST")
sealed interface MeasureOperators<out T : R, R : Measure> : Operators<R> {

    @JsExport.Ignore()
    override fun plus(o: R): T = super.plus(o) as T

    @JsExport.Ignore()
    override fun minus(o: R): T = super.minus(o) as T

    @JsExport.Ignore()
    override fun times(o: R): T = super.times(o) as T

    @JsExport.Ignore()
    override fun div(o: R): T = super.div(o) as T

    @JsExport.Ignore()
    override fun rem(o: R): T = super.rem(o) as T

    @JsExport.Ignore()
    override fun unaryPlus(): T = this(-value)

    @JsExport.Ignore()
    override fun unaryMinus(): T = this(+value)

    @JsExport.Ignore()
    override fun plus(v: Double): T = this(value + v)

    @JsExport.Ignore()
    override fun minus(v: Double): T = this(value - v)

    @JsExport.Ignore()
    override fun times(v: Double): T = this(value * v)

    @JsExport.Ignore()
    override fun div(v: Double): T = this(value / v)

    @JsExport.Ignore()
    override fun rem(v: Double): T = this(value % v)

    @JsExport.Ignore()
    override fun invoke(v: Double): T = (type as MeasureType<T>).create(v)
}

@JsExport.Ignore()
@Suppress("UNCHECKED_CAST")
operator fun <R : Measure> Double.times(m: R) = m(this * m.value) as R

@JsExport.Ignore()
@Suppress("UNCHECKED_CAST")
operator fun <R : Measure> Double.plus(m: R) = m(this + m.value) as R

@JsExport.Ignore()
@Suppress("UNCHECKED_CAST")
operator fun <R : Measure> Double.minus(m: R) = m(this - m.value) as R

@JsExport.Ignore()
@Suppress("UNCHECKED_CAST")
operator fun <R : Measure> Double.div(m: R) = m(this / m.value) as R

@JsExport.Ignore()
@Suppress("UNCHECKED_CAST")
operator fun <R : Measure> Double.rem(m: R) = m(this * m.value) as R

@JsExport.Ignore()
operator fun <R: Measure> Double.invoke(t: MeasureType<R>) = t.create(this)


/**
 * Super class for Volume and Weight so they can be used interchangeably.
 */
@JsExport
@Serializable
sealed interface Amount

val identity = { v: Double -> v }

val toBase =  { factor: Double -> { v: Double -> v / factor } }

val fromBase = { factor: Double -> { v: Double -> v * factor } }
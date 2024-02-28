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

    operator fun invoke(): Double = value
}

sealed interface Ops<out T : Measure, in R : Measure> : Measure {

    operator fun plus(o: R): T = this(type.fromBase(base + o.base))

    operator fun minus(o: R): T = this(type.fromBase(base - o.base))

    operator fun times(o: R): T = this(type.fromBase(base * o.base))

    operator fun div(o: R): T = this(type.fromBase(base / o.base))

    operator fun rem(o: R): T = this(type.fromBase(base % o.base))

    operator fun unaryPlus(): T = this(-value)

    operator fun unaryMinus(): T = this(+value)

    @JsName("plusDouble")
    operator fun plus(v: Double): T = this(value + v)

    @JsName("minusDouble")
    operator fun minus(v: Double): T = this(value - v)

    @JsName("timesDouble")
    operator fun times(v: Double): T = this(value * v)

    @JsName("divDouble")
    operator fun div(v: Double): T = this(value / v)

    @JsName("remDouble")
    operator fun rem(v: Double): T = this(value % v)

    @JsName("toType")
    @Suppress("UNCHECKED_CASt")
    operator fun invoke(v: Double): T = (type as MeasureType<T>).create(v)
}

sealed interface Operators<R : Measure> : Measure {

    operator fun plus(o: R) = this(type.fromBase(base + o.base))

    operator fun minus(o: R) = this(type.fromBase(base - o.base))

    operator fun times(o: R) = this(type.fromBase(base * o.base))

    operator fun div(o: R) = this(type.fromBase(base / o.base))

    operator fun rem(o: R) = this(type.fromBase(base % o.base))

    operator fun unaryPlus() = this(-value)

    operator fun unaryMinus() = this(+value)

    @JsName("plusDouble")
    operator fun plus(v: Double) = this(value + v)

    @JsName("minusDouble")
    operator fun minus(v: Double) = this(value - v)

    @JsName("timesDouble")
    operator fun times(v: Double) = this(value * v)

    @JsName("divDouble")
    operator fun div(v: Double) = this(value / v)

    @JsName("remDouble")
    operator fun rem(v: Double) = this(value % v)

    @JsName("toType")
    @Suppress("UNCHECKED_CASt")
    operator fun invoke(v: Double): R = (type as MeasureType<R>).create(v)
}

@Suppress("UNCHECKED_CAST")
sealed interface MeasureOperators<out T : R, R : Measure> : Operators<R> {

    override fun plus(o: R): T = super.plus(o) as T

    override fun minus(o: R): T = super.plus(o) as T

    override fun times(o: R): T = super.times(o) as T

    override fun div(o: R): T = super.div(o) as T

    override fun rem(o: R): T = super.rem(o) as T

    override fun unaryPlus(): T = this(-value)

    override fun unaryMinus(): T = this(+value)

    override fun plus(v: Double): T = this(value + v)

    override fun minus(v: Double): T = this(value - v)

    override fun times(v: Double): T = this(value * v)

    override fun div(v: Double): T = this(value / v)

    override fun rem(v: Double): T = this(value % v)

    override fun invoke(v: Double): T = (type as MeasureType<T>).create(v)
}

@Suppress("UNCHECKED_CAST")
operator fun <R : Measure> Double.times(m: R) = m.type.create(this * m.value) as R

@Suppress("UNCHECKED_CAST")
operator fun <R : Measure> Double.plus(m: R) = m.type.create(this + m.value) as R

@Suppress("UNCHECKED_CAST")
operator fun <R : Measure> Double.minus(m: R) = m.type.create(this - m.value) as R

@Suppress("UNCHECKED_CAST")
operator fun <R : Measure> Double.div(m: R) = m.type.create(this / m.value) as R

@Suppress("UNCHECKED_CAST")
operator fun <R : Measure> Double.rem(m: R) = m.type.create(this * m.value) as R

operator fun <R: Measure> Double.invoke(t: MeasureType<R>) = t.create(this)


/**
 * Super class for Volume and Weight so they can be used interchangeably.
 */
@JsExport
@Serializable
sealed interface Amount

val identity = { v: Double -> v }
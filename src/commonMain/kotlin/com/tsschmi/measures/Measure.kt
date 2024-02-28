@file:OptIn(ExperimentalSerializationApi::class, ExperimentalJsExport::class)

package com.tsschmi.measures

import kotlinx.serialization.Serializable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.reflect.cast

@JsExport
@Serializable
sealed interface MeasureType {
    val units: String
    val toBase: (Double) -> (Double)
    val fromBase: (Double) -> (Double)
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
    val type: MeasureType
    fun display(d: Int): String = "${format(d)}${type.units}"
    fun format(d: Int = 2): String = formatNumber(value, d)
}

sealed interface MeasureOperators<T, R : Measure> : Measure {

    fun create(v: Double): T

    operator fun invoke() = value

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
    operator fun invoke(v: Double) = create(v)
}

/**
 * Super class for Volume and Weight so they can be used interchangeably.
 */
@JsExport
@Serializable
sealed interface Amount

val identity = { v: Double -> v }
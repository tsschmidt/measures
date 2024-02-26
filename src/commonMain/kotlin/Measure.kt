@file:OptIn(ExperimentalSerializationApi::class, ExperimentalJsExport::class)

import kotlinx.serialization.Serializable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
@Serializable
sealed interface MeasureType<T> {
    val units: String
    val toBase: (Double) -> (Double)
    val fromBase: (Double) -> (Double)
    fun <T> create(v: Double): T
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
}

@JsExport
@Serializable
sealed class BaseMeasure : Measure {
    fun display(d: Int): String = "${format(d)}${type.units}"
    fun format(d: Int = 2): String = formatNumber(value, d)

    override fun toString(): String {
        return format(2)
    }

    operator fun invoke() = value
}

/**
 * Super class for Volume and Weight so they can be used interchangeably.
 */
@JsExport
@Serializable
sealed class Amount : BaseMeasure()

val identity = { v: Double -> v }
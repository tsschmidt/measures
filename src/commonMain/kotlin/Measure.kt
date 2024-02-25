@file:OptIn(ExperimentalSerializationApi::class, ExperimentalJsExport::class)

import kotlinx.serialization.Serializable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@JsExport
interface MeasureType {
    val units: String
    //val create: (Double) -> T
    val toBase: (Double) -> (Double)
    val fromBase: (Double) -> (Double)
}

/**
 * Defining interface for all implementations.
 */
@JsExport
@JsonClassDiscriminator("class")
sealed interface Measure {
    val value: Double
    val base: Double
    val type: MeasureType
    //val create: (Double) -> T
}

@JsExport
@JsonClassDiscriminator("class")
sealed class BaseMeasure : Measure {
    fun display(d: Int): String = "${format(d)}${type.units}"
    fun format(d: Int = 2): String = formatNumber(value, d)

    override fun toString(): String {
        return format(2)
    }

    protected abstract fun <T : Measure> create(v: Double): T

}

/**
 * Super class for Volume and Weight so they can be used interchangeably.
 */
@JsExport
@Serializable
sealed class Amount : BaseMeasure()

val identity = { v: Double -> v }
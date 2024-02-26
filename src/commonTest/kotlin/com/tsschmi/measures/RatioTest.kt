package com.tsschmi.measures

import com.tsschmi.measures.Ratio
import com.tsschmi.measures.VolumeType
import com.tsschmi.measures.WeightToVolume
import com.tsschmi.measures.WeightType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test

class RatioTest {

    @Test
    fun ratio() {
       val r: Ratio = WeightToVolume(1.5, WeightType.POUND, VolumeType.GALLON)
        println(Json.encodeToString(r))
    }
}
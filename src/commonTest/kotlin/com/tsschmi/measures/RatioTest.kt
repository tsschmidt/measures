package com.tsschmi.measures

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test

class RatioTest {

    @Test
    fun ratio() {
       val r: Ratio = WeightToVolume(1.5, WeightType.PoundType, VolumeType.GallonType)
    }
}

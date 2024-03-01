package com.tsschmi.measures

import kotlin.test.Test

class RatioTest {

    @Test
    fun ratio() {
       val r: Ratio = WeightToVolume(1.5, MassType.PoundType, VolumeType.GallonType)
    }
}

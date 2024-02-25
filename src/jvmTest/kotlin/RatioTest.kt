import org.junit.jupiter.api.Test

class RatioTest {

    @Test
    fun ratio() {
        println(WeightToVolume(1.5, WeightType.POUND, VolumeType.GALLON).convert(WeightType.OUNCE, VolumeType.QUART))
    }
}

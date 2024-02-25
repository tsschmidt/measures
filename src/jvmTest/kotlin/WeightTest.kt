import org.junit.jupiter.api.Test


class WeightTest {

    @Test
    fun testEquals() {
        val k = Kilogram(1.0)
        val k1: Kilogram = k + Kilogram(2.0)
        println(k1)
    }

    /*
    @Test
    fun getBase() {
    }

    @Test
    fun getKilogram() {
    }

    @Test
    fun getGram() {
    }

    @Test
    fun getPound() {
    }

    @Test
    fun getOunce() {
    }

    @Test
    fun toKilograms() {
    }

    @Test
    fun toGram() {
    }

    @Test
    fun toPound() {
    }

    @Test
    fun toOunce() {
    }

    @Test
    fun testToString() {
    }

    @Test
    fun getType() {
    }
     */
}

actual fun formatNumber(value: Double, digits: Int): String {
    return value.asDynamic().toFixed(digits) as String
}


package com.tsschmi.measures
actual fun formatNumber(value: Double, digits: Int): String {
    return "%.${digits}f".format(value)
}


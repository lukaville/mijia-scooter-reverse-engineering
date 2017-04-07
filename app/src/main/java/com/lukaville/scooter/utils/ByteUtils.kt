package com.lukaville.scooter.utils

fun Byte.toUnsignedInt(): Int {
    return toInt() and 0xFF
}
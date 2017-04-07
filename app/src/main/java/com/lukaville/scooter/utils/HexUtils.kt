package com.lukaville.scooter.utils

private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()

fun ByteArray.toHexString(): String {
    val hexChars = CharArray(size * 2)

    for (j in indices) {
        val v = get(j).toInt() and 0xFF
        hexChars[j * 2] = HEX_ARRAY[v ushr 4]
        hexChars[j * 2 + 1] = HEX_ARRAY[v and 0x0F]
    }

    return String(hexChars)
}

fun String.toBytes(): ByteArray {
    if (length % 2 == 1) {
        throw IllegalArgumentException("hexToBytes requires an even-length String parameter")
    }

    val data = ByteArray(length / 2)

    var i = 0
    while (i < length) {
        data[i / 2] = ((Character.digit(get(i), 16) shl 4) + Character.digit(get(i + 1), 16)).toByte()
        i += 2
    }

    return data
}